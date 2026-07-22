package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.domain.subscription.Subscription;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.OfferRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.repositories.SubscriptionRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.SubscriptionSpecifications;
import ma.s2m.nxp.fe.settings.services.ISubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class SubscriptionService implements ISubscriptionService {

    private static final String SUBSCRIPTION_ID_PREFIX = "SUB-";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final ProgramRepository programRepository;
    private final OfferRepository offerRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               CustomerRepository customerRepository,
                               ProgramRepository programRepository,
                               OfferRepository offerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.customerRepository = customerRepository;
        this.programRepository = programRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Subscription> getAllSubscriptions(Pageable pageable, String customerEmail) {
        Specification<Subscription> spec = (root, query, cb) -> cb.conjunction();

        if (customerEmail != null && !customerEmail.isBlank()) {
            spec = spec.and(SubscriptionSpecifications.hasCustomerEmailLike(customerEmail));
        }

        return subscriptionRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public Subscription createSubscription(Subscription subscription, Long customerId, Long programId, Long offerId)
            throws BusinessException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("SUB_003", "Customer not found", HttpStatus.NOT_FOUND));

        if (subscriptionRepository.existsByCustomerId(customerId)) {
            throw new BusinessException("SUB_006", "Ce client possède déjà une souscription", HttpStatus.CONFLICT);
        }

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new BusinessException("SUB_004", "Program not found", HttpStatus.NOT_FOUND));

        subscription.setCustomer(customer);
        subscription.setCustomerEmail(customer.getEmail());
        subscription.setProgram(program);
        subscription.setOffer(resolveOffer(offerId));
        subscription.setSubscriptionId(generateUniqueSubscriptionId());

        log.info("Creating subscription customerId={} programId={} subscriptionId={}",
                customerId, programId, subscription.getSubscriptionId());
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscription(Long id, Subscription newData, Long customerId, Long programId, Long offerId)
            throws BusinessException {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("SUB_002", "Subscription not found", HttpStatus.NOT_FOUND));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("SUB_003", "Customer not found", HttpStatus.NOT_FOUND));
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new BusinessException("SUB_004", "Program not found", HttpStatus.NOT_FOUND));

        existing.setCustomer(customer);
        existing.setCustomerEmail(customer.getEmail());
        existing.setProgram(program);
        existing.setOffer(resolveOffer(offerId));
        existing.setSubscriptionDate(newData.getSubscriptionDate());
        existing.setMode(newData.getMode());
        existing.setStatus(newData.getStatus());
        existing.setEligibility(newData.getEligibility());
        // subscriptionId n'est jamais modifiable après création

        log.info("Updating subscription id={}", id);
        return subscriptionRepository.save(existing);
    }

    @Override
    public void deleteSubscription(Long id) throws BusinessException {
        if (!subscriptionRepository.existsById(id)) {
            throw new BusinessException("SUB_002", "Subscription not found", HttpStatus.NOT_FOUND);
        }
        subscriptionRepository.deleteById(id);
        log.info("Deleted subscription id={}", id);
    }

    private Offer resolveOffer(Long offerId) throws BusinessException {
        if (offerId == null) {
            return null;
        }
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new BusinessException("SUB_005", "Offer not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Génère un identifiant métier unique du type SUB-483920.
     */
    private String generateUniqueSubscriptionId() {
        String candidate;
        do {
            candidate = SUBSCRIPTION_ID_PREFIX + (100000 + RANDOM.nextInt(900000));
        } while (subscriptionRepository.existsBySubscriptionId(candidate));
        return candidate;
    }
}