package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.customer.CustomerDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.CustomersPageResponse;

import java.util.List;

public interface ICustomerOrchestrationService {

    CustomersPageResponse getAllCustomers(int page, int limit, String lastName,String email, String subBin);

    CustomerDTO getCustomerById(Long id) throws BusinessException;

    CustomerDTO createCustomer(CustomerDTO dto) throws BusinessException;

    CustomerDTO updateCustomer(Long id, CustomerDTO dto) throws BusinessException;

    void deleteCustomer(Long id) throws BusinessException;

    List<CustomerDTO> getAllCustomersForExport(String lastName,String email , String subBin);
}