package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.customer.CustomerDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.CustomersPageResponse;

public interface ICustomerOrchestrationService {

    CustomersPageResponse getAllCustomers(int page, int limit, String name,String email, String subBin);

    CustomerDTO getCustomerById(Long id) throws BusinessException;

    CustomerDTO createCustomer(CustomerDTO dto) throws BusinessException;

    CustomerDTO updateCustomer(Long id, CustomerDTO dto) throws BusinessException;

    void deleteCustomer(Long id) throws BusinessException;
}