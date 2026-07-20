package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.dto.installment.CustomerInstallmentDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.CustomerInstallmentMapper;
import ma.s2m.nxp.fe.settings.orchestration.ICustomerInstallmentOrchestrationService;
import ma.s2m.nxp.fe.settings.services.ICustomerInstallmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInstallmentOrchestrationServiceImpl implements ICustomerInstallmentOrchestrationService {

    private final ICustomerInstallmentService installmentService;
    private final CustomerInstallmentMapper installmentMapper;

    public CustomerInstallmentOrchestrationServiceImpl(ICustomerInstallmentService installmentService,
                                                       CustomerInstallmentMapper installmentMapper) {
        this.installmentService = installmentService;
        this.installmentMapper = installmentMapper;
    }

    @Override
    public CustomerInstallmentsPageResponse getAllInstallments(int page, int limit, String customerName,
                                                               String customerEmail, Long customerId) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.ASC, "dueDate"));

        Page<CustomerInstallment> result = installmentService.getAllInstallments(
                pageRequest, customerName, customerEmail, customerId);

        List<CustomerInstallmentDTO> content = result.getContent().stream()
                .map(installmentMapper::toDTO)
                .toList();

        return new CustomerInstallmentsPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public CustomerInstallmentDTO getInstallmentById(Long id) throws BusinessException {
        CustomerInstallment installment = installmentService.getInstallmentById(id)
                .orElseThrow(() -> new BusinessException("CIT_002", "Installment not found", HttpStatus.NOT_FOUND));
        return installmentMapper.toDTO(installment);
    }

    @Override
    public CustomerInstallmentDTO createInstallment(CustomerInstallmentDTO dto) throws BusinessException {
        CustomerInstallment installment = installmentMapper.toEntity(dto);
        CustomerInstallment saved = installmentService.createInstallment(installment, dto.getCustomerId());
        return installmentMapper.toDTO(saved);
    }

    @Override
    public CustomerInstallmentDTO updateInstallment(Long id, CustomerInstallmentDTO dto) throws BusinessException {
        CustomerInstallment newData = installmentMapper.toEntity(dto);
        CustomerInstallment updated = installmentService.updateInstallment(id, newData, dto.getCustomerId());
        return installmentMapper.toDTO(updated);
    }

    @Override
    public void deleteInstallment(Long id) throws BusinessException {
        installmentService.deleteInstallment(id);
    }
}