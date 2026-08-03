package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.operation.OperationDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.OperationsPageResponse;

import java.util.List;

public interface IOperationOrchestrationService {

    OperationsPageResponse getAllOperations(int page, int limit, String reference, String email, String programName);

    OperationDTO getOperationById(Long id) throws BusinessException;

    OperationDTO createOperation(OperationDTO dto) throws BusinessException;

    OperationDTO updateOperation(Long id, OperationDTO dto) throws BusinessException;

    void deleteOperation(Long id) throws BusinessException;
    List<OperationDTO> getAllOperationsForExport(String reference, String email, String programName);
}