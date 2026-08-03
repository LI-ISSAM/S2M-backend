package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.enums.CustomerInstallmentStatus;
import ma.s2m.nxp.fe.settings.domain.installment_plan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CustomerInstallmentRepository;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.InstallmentPlanRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.CustomerInstallmentSpecifications;
import ma.s2m.nxp.fe.settings.services.ICustomerInstallmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CustomerInstallmentService implements ICustomerInstallmentService {

    private final CustomerInstallmentRepository installmentRepository;
    private final CustomerRepository customerRepository;
    private final InstallmentPlanRepository installmentPlanRepository;

    public CustomerInstallmentService(CustomerInstallmentRepository installmentRepository,
                                      CustomerRepository customerRepository,
                                      InstallmentPlanRepository installmentPlanRepository) {
        this.installmentRepository = installmentRepository;
        this.customerRepository = customerRepository;
        this.installmentPlanRepository = installmentPlanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerInstallment> getAllInstallments(Pageable pageable, String customerName, String customerEmail,
                                                        Long customerId) {
        Specification<CustomerInstallment> spec = (root, query, cb) -> cb.conjunction();


        if (customerName != null && !customerName.isBlank()) {
            spec = spec.and(CustomerInstallmentSpecifications.hasCustomerNameLike(customerName));
        }
        if (customerEmail != null && !customerEmail.isBlank()) {
            spec = spec.and(CustomerInstallmentSpecifications.hasCustomerEmailLike(customerEmail));
        }
        if (customerId != null) {
            spec = spec.and(CustomerInstallmentSpecifications.hasCustomerId(customerId));
        }

        return installmentRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerInstallment> getInstallmentById(Long id) {
        return installmentRepository.findById(id);
    }

    @Override
    public CustomerInstallment createInstallment(CustomerInstallment installment, Long customerId)
            throws BusinessException {
        Customer customer = resolveCustomer(customerId);

        if (existsForMonth(customerId, installment.getDueDate())) {
            throw new BusinessException("CIT_005",
                    "This customer already has an installment for this month", HttpStatus.CONFLICT);
        }

        installment.setCustomer(customer);
        log.info("Creating installment for customerId={} dueDate={} amount={}",
                customerId, installment.getDueDate(), installment.getAmount());
        CustomerInstallment saved = installmentRepository.save(installment);

        if (saved.getStatus() == CustomerInstallmentStatus.PAID) {
            generateNextInstallmentIfAbsent(saved);
        }

        return saved;
    }

    /**
     * Le client et la date d'échéance sont verrouillés après création :
     * modifier la date d'une échéance existante revient à faire disparaître
     * le mois précédent (même ligne en base). Seuls amount/status sont modifiables.
     * Quand le statut passe à PAID, l'échéance du mois suivant est générée
     * automatiquement (même client, montant tiré du plan si disponible),
     * ce qui évite d'avoir à cliquer "Ajouter" à chaque mois.
     */
    @Override
    public CustomerInstallment updateInstallment(Long id, CustomerInstallment newData, Long customerId)
            throws BusinessException {
        CustomerInstallment existing = installmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CIT_002", "Installment not found", HttpStatus.NOT_FOUND));

        if (customerId != null && !customerId.equals(existing.getCustomer().getId())) {
            throw new BusinessException("CIT_006",
                    "Customer cannot be changed on an existing installment", HttpStatus.CONFLICT);
        }

        if (newData.getDueDate() != null && !YearMonth.from(newData.getDueDate())
                .equals(YearMonth.from(existing.getDueDate()))) {
            throw new BusinessException("CIT_007",
                    "Due date cannot be changed once created. Mark it as PAID instead: "
                            + "the next month's installment will be generated automatically.",
                    HttpStatus.CONFLICT);
        }

        boolean wasNotPaid = existing.getStatus() != CustomerInstallmentStatus.PAID;
        boolean isNowPaid = newData.getStatus() == CustomerInstallmentStatus.PAID;

        existing.setAmount(newData.getAmount());
        existing.setStatus(newData.getStatus());

        CustomerInstallment saved = installmentRepository.save(existing);
        log.info("Updating installment id={} status={}", id, saved.getStatus());

        if (wasNotPaid && isNowPaid) {
            generateNextInstallmentIfAbsent(saved);
        }

        return saved;
    }

    @Override
    public void deleteInstallment(Long id) throws BusinessException {
        if (!installmentRepository.existsById(id)) {
            throw new BusinessException("CIT_002", "Installment not found", HttpStatus.NOT_FOUND);
        }
        installmentRepository.deleteById(id);
        log.info("Deleted installment id={}", id);
    }

    /**
     * Crée l'échéance du mois suivant pour le même client, si elle n'existe
     * pas déjà. Le montant est repris du plan de mensualités du client si un
     * échéancier correspondant est trouvé pour ce mois ; sinon le montant de
     * l'échéance qui vient d'être payée est réutilisé.
     */
    private void generateNextInstallmentIfAbsent(CustomerInstallment paidInstallment) {
        LocalDate nextDueDate = paidInstallment.getDueDate().plusMonths(1);
        Long customerId = paidInstallment.getCustomer().getId();

        if (existsForMonth(customerId, nextDueDate)) {
            log.info("Next installment for customerId={} month={} already exists, skipping generation",
                    customerId, YearMonth.from(nextDueDate));
            return;
        }

        BigDecimal nextAmount = resolveNextAmount(customerId, nextDueDate, paidInstallment.getAmount());

        CustomerInstallment next = CustomerInstallment.builder()
                .customer(paidInstallment.getCustomer())
                .dueDate(nextDueDate)
                .amount(nextAmount)
                .status(CustomerInstallmentStatus.PENDING)
                .build();

        installmentRepository.save(next);
        log.info("Auto-generated next installment for customerId={} dueDate={} amount={}",
                customerId, nextDueDate, nextAmount);
    }

    private BigDecimal resolveNextAmount(Long customerId, LocalDate nextDueDate, BigDecimal fallbackAmount) {
        Optional<InstallmentPlan> planOpt = installmentPlanRepository.findFirstByCustomer_IdOrderByStartDateDesc(customerId);
        if (planOpt.isEmpty()) {
            return fallbackAmount;
        }

        YearMonth targetMonth = YearMonth.from(nextDueDate);
        return planOpt.get().getInstallments().stream()
                .filter(i -> YearMonth.from(i.getDueDate()).equals(targetMonth))
                .map(i -> i.getAmount())
                .findFirst()
                .orElse(fallbackAmount);
    }

    private boolean existsForMonth(Long customerId, LocalDate dueDate) {
        if (customerId == null || dueDate == null) {
            return false;
        }
        YearMonth targetMonth = YearMonth.from(dueDate);
        List<CustomerInstallment> existing = installmentRepository.findAllByCustomer_Id(customerId);
        return existing.stream().anyMatch(i -> YearMonth.from(i.getDueDate()).equals(targetMonth));
    }

    private Customer resolveCustomer(Long customerId) throws BusinessException {
        if (customerId == null) {
            throw new BusinessException("CIT_003", "Customer is required", HttpStatus.BAD_REQUEST);
        }
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CIT_004", "Customer not found", HttpStatus.NOT_FOUND));
    }
}