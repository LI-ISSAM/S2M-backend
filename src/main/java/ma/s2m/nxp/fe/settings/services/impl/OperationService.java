package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.domain.operation.Operation;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.MerchantRepository;
import ma.s2m.nxp.fe.settings.repositories.OperationRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.repositories.specifications.OperationSpecifications;
import ma.s2m.nxp.fe.settings.services.IOperationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OperationService implements IOperationService {

    private final OperationRepository operationRepository;
    private final MerchantRepository merchantRepository;
    private final ProgramRepository programRepository;
    private final CustomerRepository customerRepository;

    public OperationService(OperationRepository operationRepository, MerchantRepository merchantRepository,
                            ProgramRepository programRepository, CustomerRepository customerRepository) {
        this.operationRepository = operationRepository;
        this.merchantRepository = merchantRepository;
        this.programRepository = programRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Operation> getAllOperations(Pageable pageable, String reference, String email, String programName) {
        Specification<Operation> spec =  (root, query, cb) -> cb.conjunction();

        if (reference != null && !reference.isBlank()) {
            spec = spec.and(OperationSpecifications.hasMerchantReferenceLike(reference));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and(OperationSpecifications.hasCustomerEmailLike(email));
        }
        if (programName != null && !programName.isBlank()) {
            spec = spec.and(OperationSpecifications.hasProgramNameLike(programName));
        }

        return operationRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Operation> getOperationById(Long id) {
        return operationRepository.findById(id);
    }

    @Override
    public Operation createOperation(Operation operation, Long merchantId, Long bnplProgramId, String customerEmail)
            throws BusinessException {
        Merchant merchant = resolveMerchant(merchantId);
        Program program = resolveProgram(bnplProgramId);
        Customer customer = resolveCustomerByEmail(customerEmail);

        operation.setMerchant(merchant);
        operation.setBnplProgram(program);
        operation.setCustomer(customer);

        log.info("Creating operation rrn={} stan={} for merchantId={} customerEmail={}",
                operation.getRrn(), operation.getStan(), merchantId, customerEmail);
        return operationRepository.save(operation);
    }

    @Override
    public Operation updateOperation(Long id, Operation newData, Long merchantId, Long bnplProgramId,
                                     String customerEmail) throws BusinessException {
        Operation existing = operationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("OPR_002", "Operation not found", HttpStatus.NOT_FOUND));

        Merchant merchant = resolveMerchant(merchantId);
        Program program = resolveProgram(bnplProgramId);
        Customer customer = resolveCustomerByEmail(customerEmail);

        existing.setPan(newData.getPan());
        existing.setIssuingBank(newData.getIssuingBank());
        existing.setAcquiring(newData.getAcquiring());
        existing.setRrn(newData.getRrn());
        existing.setStan(newData.getStan());
        existing.setMerchant(merchant);
        existing.setAmount(newData.getAmount());
        existing.setCurrency(newData.getCurrency());
        existing.setTransactionTime(newData.getTransactionTime());
        existing.setBnplProgram(program);
        existing.setCustomer(customer);
        existing.setNumberOfInstallments(newData.getNumberOfInstallments());

        log.info("Updating operation id={}", id);
        return operationRepository.save(existing);
    }

    @Override
    public void deleteOperation(Long id) throws BusinessException {
        if (!operationRepository.existsById(id)) {
            throw new BusinessException("OPR_002", "Operation not found", HttpStatus.NOT_FOUND);
        }
        operationRepository.deleteById(id);
        log.info("Deleted operation id={}", id);
    }

    private Merchant resolveMerchant(Long merchantId) throws BusinessException {
        if (merchantId == null) {
            throw new BusinessException("OPR_003", "Merchant is required", HttpStatus.BAD_REQUEST);
        }
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new BusinessException("OPR_004", "Merchant not found", HttpStatus.NOT_FOUND));
    }

    private Program resolveProgram(Long programId) throws BusinessException {
        if (programId == null) {
            throw new BusinessException("OPR_005", "BNPL Program is required", HttpStatus.BAD_REQUEST);
        }
        return programRepository.findById(programId)
                .orElseThrow(() -> new BusinessException("OPR_006", "BNPL Program not found", HttpStatus.NOT_FOUND));
    }

    private Customer resolveCustomerByEmail(String email) throws BusinessException {
        if (email == null || email.isBlank()) {
            throw new BusinessException("OPR_007", "Customer Email is required", HttpStatus.BAD_REQUEST);
        }
        return customerRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BusinessException("OPR_008", "Customer not found for this email", HttpStatus.NOT_FOUND));
    }
}