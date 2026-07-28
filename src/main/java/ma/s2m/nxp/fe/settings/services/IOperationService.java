package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.operation.Operation;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IOperationService {

    Page<Operation> getAllOperations(Pageable pageable, String reference, String email, String programName);

    Optional<Operation> getOperationById(Long id);

    Operation createOperation(Operation operation, Long merchantId, Long bnplProgramId, String customerEmail)
            throws BusinessException;

    Operation updateOperation(Long id, Operation newData, Long merchantId, Long bnplProgramId, String customerEmail)
            throws BusinessException;

    void deleteOperation(Long id) throws BusinessException;
}