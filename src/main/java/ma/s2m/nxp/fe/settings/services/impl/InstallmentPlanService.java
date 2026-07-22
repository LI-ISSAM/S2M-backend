package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.installmentplan.Installment;
import ma.s2m.nxp.fe.settings.domain.installmentplan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.Enums.InstallmentStatus;
import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.InstallmentPlanRepository;
import ma.s2m.nxp.fe.settings.repositories.OfferRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.InstallmentPlanSpecifications;
import ma.s2m.nxp.fe.settings.services.IInstallmentPlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class InstallmentPlanService implements IInstallmentPlanService {

    private final InstallmentPlanRepository installmentPlanRepository;
    private final CustomerRepository customerRepository;
    private final OfferRepository offerRepository;

    public InstallmentPlanService(InstallmentPlanRepository installmentPlanRepository,
                                  CustomerRepository customerRepository,
                                  OfferRepository offerRepository) {
        this.installmentPlanRepository = installmentPlanRepository;
        this.customerRepository = customerRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstallmentPlan> getAllInstallmentPlans(Pageable pageable, String customerName, String offerName,
                                                        Long customerId, Long offerId) {
        Specification<InstallmentPlan> spec =  (root, query, cb) -> cb.conjunction();


        if (customerName != null && !customerName.isBlank()) {
            spec = spec.and(InstallmentPlanSpecifications.hasCustomerNameLike(customerName));
        }
        if (offerName != null && !offerName.isBlank()) {
            spec = spec.and(InstallmentPlanSpecifications.hasOfferNameLike(offerName));
        }
        if (customerId != null) {
            spec = spec.and(InstallmentPlanSpecifications.hasCustomerId(customerId));
        }
        if (offerId != null) {
            spec = spec.and(InstallmentPlanSpecifications.hasOfferId(offerId));
        }

        return installmentPlanRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstallmentPlan> getInstallmentPlanById(Long id) {
        return installmentPlanRepository.findById(id);
    }

    @Override
    public InstallmentPlan createInstallmentPlan(InstallmentPlan plan, Long customerId, Long offerId)
            throws BusinessException {
        Customer customer = resolveCustomer(customerId);
        Offer offer = resolveOfferIfPresent(offerId);

        plan.setCustomer(customer);
        plan.setCustomerEmail(customer.getEmail());
        plan.setOffer(offer);
        plan.setInstallments(generateSchedule(plan));

        log.info("Creating installment plan for customerId={} totalAmount={} installments={}",
                customerId, plan.getTotalAmount(), plan.getNumberOfInstallments());
        return installmentPlanRepository.save(plan);
    }

    @Override
    public InstallmentPlan updateInstallmentPlan(Long id, InstallmentPlan newData, Long customerId, Long offerId)
            throws BusinessException {
        InstallmentPlan existing = installmentPlanRepository.findById(id)
                .orElseThrow(() -> new BusinessException("IPL_002", "Installment Plan not found", HttpStatus.NOT_FOUND));

        Customer customer = resolveCustomer(customerId);
        Offer offer = resolveOfferIfPresent(offerId);

        existing.setCustomer(customer);
        existing.setOffer(offer);
        existing.setCustomerEmail(customer.getEmail());
        existing.setTotalAmount(newData.getTotalAmount());
        existing.setNumberOfInstallments(newData.getNumberOfInstallments());
        existing.setStartDate(newData.getStartDate());
        existing.setStatus(newData.getStatus());

        // Régénère l'échéancier si le montant, le nombre d'échéances ou la date
        // de début ont changé (orphanRemoval=true côté entité supprime les anciennes lignes).
        existing.getInstallments().clear();
        installmentPlanRepository.flush();
        existing.getInstallments().addAll(generateSchedule(existing));

        log.info("Updating installment plan id={}", id);
        return installmentPlanRepository.save(existing);
    }

    @Override
    public void deleteInstallmentPlan(Long id) throws BusinessException {
        if (!installmentPlanRepository.existsById(id)) {
            throw new BusinessException("IPL_002", "Installment Plan not found", HttpStatus.NOT_FOUND);
        }
        installmentPlanRepository.deleteById(id);
        log.info("Deleted installment plan id={}", id);
    }

    /**
     * Génère l'échéancier : même logique que le calcul côté frontend
     * (montant de base arrondi à l'inférieur, reliquat ajouté à la dernière
     * échéance, échéances mensuelles à partir de startDate), mais persistée
     * côté backend comme source de vérité.
     */
    private List<Installment> generateSchedule(InstallmentPlan plan) {
        List<Installment> schedule = new ArrayList<>();

        BigDecimal total = plan.getTotalAmount();
        int count = plan.getNumberOfInstallments();
        LocalDate start = plan.getStartDate();

        if (total == null || count <= 0 || start == null) {
            return schedule;
        }

        BigDecimal baseAmount = total.divide(BigDecimal.valueOf(count), 2, RoundingMode.DOWN);
        BigDecimal remainder = total.subtract(baseAmount.multiply(BigDecimal.valueOf(count)))
                .setScale(2, RoundingMode.HALF_UP);

        for (int i = 0; i < count; i++) {
            boolean isLast = (i == count - 1);
            BigDecimal amount = isLast ? baseAmount.add(remainder) : baseAmount;

            schedule.add(Installment.builder()
                    .number(i + 1)
                    .dueDate(start.plusMonths(i))
                    .amount(amount)
                    .status(InstallmentStatus.PENDING)
                    .installmentPlan(plan)
                    .build());
        }

        return schedule;
    }

    private Customer resolveCustomer(Long customerId) throws BusinessException {
        if (customerId == null) {
            throw new BusinessException("IPL_003", "Customer is required", HttpStatus.BAD_REQUEST);
        }
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("IPL_004", "Customer not found", HttpStatus.NOT_FOUND));
    }

    private Offer resolveOfferIfPresent(Long offerId) throws BusinessException {
        if (offerId == null) {
            return null;
        }
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new BusinessException("IPL_005", "Offer not found", HttpStatus.NOT_FOUND));
    }
}