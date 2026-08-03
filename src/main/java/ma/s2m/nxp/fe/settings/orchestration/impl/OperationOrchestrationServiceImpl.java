package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.operation.Operation;
import ma.s2m.nxp.fe.settings.dto.operation.OperationDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.OperationMapper;
import ma.s2m.nxp.fe.settings.orchestration.IOperationOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IOperationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationOrchestrationServiceImpl implements IOperationOrchestrationService {

    private final IOperationService operationService;
    private final OperationMapper operationMapper;

    public OperationOrchestrationServiceImpl(IOperationService operationService, OperationMapper operationMapper) {
        this.operationService = operationService;
        this.operationMapper = operationMapper;
    }

    @Override
    public OperationsPageResponse getAllOperations(int page, int limit, String reference, String email, String programName) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "transactionTime"));

        Page<Operation> result = operationService.getAllOperations(pageRequest, reference, email, programName);

        List<OperationDTO> content = result.getContent().stream()
                .map(operationMapper::toDTO)
                .toList();

        return new OperationsPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }
    @Override
    public List<OperationDTO> getAllOperationsForExport(String reference, String email, String programName) {
        Page<Operation> result = operationService.getAllOperations(Pageable.unpaged(), reference, email, programName);
        return result.getContent().stream()
                .map(operationMapper::toDTO)
                .toList();
    }

    @Override
    public OperationDTO getOperationById(Long id) throws BusinessException {
        Operation operation = operationService.getOperationById(id)
                .orElseThrow(() -> new BusinessException("OPR_002", "Operation not found", HttpStatus.NOT_FOUND));
        return operationMapper.toDTO(operation);
    }

    @Override
    public OperationDTO createOperation(OperationDTO dto) throws BusinessException {
        Operation operation = operationMapper.toEntity(dto);
        Operation saved = operationService.createOperation(operation, dto.getMerchantId(), dto.getBnplProgramId(), dto.getCustomerEmail());
        return operationMapper.toDTO(saved);
    }

    @Override
    public OperationDTO updateOperation(Long id, OperationDTO dto) throws BusinessException {
        Operation newData = operationMapper.toEntity(dto);
        Operation updated = operationService.updateOperation(id, newData, dto.getMerchantId(), dto.getBnplProgramId(), dto.getCustomerEmail());
        return operationMapper.toDTO(updated);
    }

    @Override
    public void deleteOperation(Long id) throws BusinessException {
        operationService.deleteOperation(id);
    }
}