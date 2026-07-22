package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CardRepository;
import ma.s2m.nxp.fe.settings.repositories.CustomerInstallmentRepository;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.InstallmentPlanRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.CustomerSpecifications;
import ma.s2m.nxp.fe.settings.services.ICustomerService;
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
public class CustomerService implements ICustomerService {

    private static final String CUSTOMER_ID_PREFIX = "CST-";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CustomerRepository customerRepository;
    private final CardRepository cardRepository;
    private final InstallmentPlanRepository installmentPlanRepository;
    private final CustomerInstallmentRepository customerInstallmentRepository;

    public CustomerService(CustomerRepository customerRepository, CardRepository cardRepository,
                           InstallmentPlanRepository installmentPlanRepository,
                           CustomerInstallmentRepository customerInstallmentRepository) {
        this.customerRepository = customerRepository;
        this.cardRepository = cardRepository;
        this.installmentPlanRepository = installmentPlanRepository;
        this.customerInstallmentRepository = customerInstallmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getAllCustomers(Pageable pageable, String lastName , String email) {
        Specification<Customer> spec  = (root, query, cb) -> cb.conjunction();

        if (lastName != null && !lastName.isBlank()) {
            spec = spec.and(CustomerSpecifications.hasFullNameLike(lastName));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and(CustomerSpecifications.hasEmailLike(email));
        }

        return customerRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer createCustomer(Customer customer) throws BusinessException {
        if (customerRepository.existsByEmailIgnoreCase(customer.getEmail())) {
            throw new BusinessException("CST_001",
                    "A customer with this email already exists", HttpStatus.CONFLICT);
        }

        customer.setCustomerId(generateUniqueCustomerId());
        log.info("Creating customer fullName={} customerId={}", customer.getFullName(), customer.getCustomerId());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer newData) throws BusinessException {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CST_002", "Customer not found", HttpStatus.NOT_FOUND));

        if (customerRepository.existsByEmailIgnoreCaseAndIdNot(newData.getEmail(), id)) {
            throw new BusinessException("CST_001",
                    "A customer with this email already exists", HttpStatus.CONFLICT);
        }

        // 1. Customer Data
        existing.setBank(newData.getBank());
        existing.setBranch(newData.getBranch());
        existing.setClientId(newData.getClientId());
        existing.setVipCategory(newData.getVipCategory());
        existing.setTitle(newData.getTitle());
        existing.setFirstName(newData.getFirstName());
        existing.setMiddleName(newData.getMiddleName());
        existing.setLastName(newData.getLastName());
        existing.setBirthDate(newData.getBirthDate());
        existing.setBirthPlace(newData.getBirthPlace());
        existing.setPrimaryIdType(newData.getPrimaryIdType());
        existing.setPrimaryId(newData.getPrimaryId());
        existing.setSecondaryIdType(newData.getSecondaryIdType());
        existing.setSecondaryId(newData.getSecondaryId());
        existing.setGender(newData.getGender());
        existing.setMaritalStatus(newData.getMaritalStatus());
        existing.setNationality(newData.getNationality());
        existing.setDependents(newData.getDependents());
        existing.setPassportExpiryDate(newData.getPassportExpiryDate());
        existing.setOwnersList(newData.getOwnersList());
        existing.setCustomerSegment(newData.getCustomerSegment());
        existing.setCompany(newData.getCompany());
        existing.setCustomerCurrency(newData.getCustomerCurrency());
        existing.setSubBin(newData.getSubBin());
        existing.setIdentityFile(newData.getIdentityFile());

        // 2. Customer Information
        existing.setParentClient(newData.getParentClient());
        existing.setParentRelation(newData.getParentRelation());
        existing.setCustomerCreationDate(newData.getCustomerCreationDate());
        existing.setResolvabilityLevel(newData.getResolvabilityLevel());
        existing.setStatus(newData.getStatus());
        existing.setStatusDate(newData.getStatusDate());
        existing.setStatusReason(newData.getStatusReason());
        existing.setDebitCard(newData.getDebitCard());
        existing.setCreditCard(newData.getCreditCard());
        existing.setPrepaidCard(newData.getPrepaidCard());
        existing.setPhoneNumber(newData.getPhoneNumber());
        existing.setEmail(newData.getEmail());

        // 3. Professional Information
        existing.setEmployeeCode(newData.getEmployeeCode());
        existing.setEmployeeName(newData.getEmployeeName());
        existing.setPosition(newData.getPosition());
        existing.setGrossIncome(newData.getGrossIncome());
        existing.setNetIncome(newData.getNetIncome());
        existing.setSalary(newData.getSalary());
        existing.setRiskLevel(newData.getRiskLevel());

        // 5. Account (valeurs par défaut)
        existing.setDefaultMxpAccount(newData.getDefaultMxpAccount());
        existing.setDefaultBankAccount(newData.getDefaultBankAccount());

        // Tableaux : on remplace intégralement le contenu (orphanRemoval implicite
        // via @ElementCollection, les anciennes lignes sont supprimées et
        // recréées à chaque sauvegarde).
        existing.getAddresses().clear();
        existing.getAddresses().addAll(newData.getAddresses());
        existing.getAccounts().clear();
        existing.getAccounts().addAll(newData.getAccounts());
        existing.getCards().clear();
        existing.getCards().addAll(newData.getCards());
        existing.getRoutings().clear();
        existing.getRoutings().addAll(newData.getRoutings());
        existing.getLinks().clear();
        existing.getLinks().addAll(newData.getLinks());
        // customerId n'est jamais modifiable après création

        log.info("Updating customer id={}", id);
        return customerRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) throws BusinessException {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException("CST_002", "Customer not found", HttpStatus.NOT_FOUND);
        }

        if (cardRepository.existsByCustomer_Id(id)) {
            throw new BusinessException("CST_005",
                    "Cannot delete this customer: it still has card(s) attached. Please delete them first.",
                    HttpStatus.CONFLICT);
        }
        if (installmentPlanRepository.existsByCustomer_Id(id)) {
            throw new BusinessException("CST_006",
                    "Cannot delete this customer: it still has installment plan(s) attached. Please delete them first.",
                    HttpStatus.CONFLICT);
        }
        if (customerInstallmentRepository.existsByCustomer_Id(id)) {
            throw new BusinessException("CST_007",
                    "Cannot delete this customer: it still has installment(s) attached. Please delete them first.",
                    HttpStatus.CONFLICT);
        }

        customerRepository.deleteById(id);
        log.info("Deleted customer id={}", id);
    }

    /**
     * Génère un identifiant métier unique du type CST-483920.
     * Boucle par sécurité en cas de collision improbable.
     */
    private String generateUniqueCustomerId() {
        String candidate;
        do {
            candidate = CUSTOMER_ID_PREFIX + (100000 + RANDOM.nextInt(900000));
        } while (customerRepository.existsByCustomerId(candidate));
        return candidate;
    }
}