package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.installment.CustomerInstallmentDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.CustomerInstallmentsPageResponse;

import java.util.List;

public interface ICustomerInstallmentOrchestrationService {

    CustomerInstallmentsPageResponse getAllInstallments(int page, int limit, String customerName,
                                                        String customerEmail, Long customerId);

    List<CustomerInstallmentDTO> getAllInstallmentsForExport(String customerName , String customerEmail, Long customerId);

    CustomerInstallmentDTO getInstallmentById(Long id) throws BusinessException;

    CustomerInstallmentDTO createInstallment(CustomerInstallmentDTO dto) throws BusinessException;

    CustomerInstallmentDTO updateInstallment(Long id, CustomerInstallmentDTO dto) throws BusinessException;

    void deleteInstallment(Long id) throws BusinessException;
}