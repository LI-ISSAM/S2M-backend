package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.DTO.customer.CustomerDTO;
import ma.s2m.nxp.fe.settings.domain.member.Institution;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.CustomerMapper;
import ma.s2m.nxp.fe.settings.orchestration.ICustomerOrchestrationService;
import ma.s2m.nxp.fe.settings.services.ICustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrchestrationServiceImpl implements ICustomerOrchestrationService {

    private final ICustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerOrchestrationServiceImpl(ICustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomersPageResponse getAllCustomers(int page, int limit, String name,String email, String subBin) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.ASC, "fullName"));

        Page<Customer> result = customerService.getAllCustomers(pageRequest, name,email);

        List<CustomerDTO> content = result.getContent().stream()
                .map(customerMapper::toDTO)
                .toList();

        return new CustomersPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) throws BusinessException {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new BusinessException("CST_002", "Customer not found", HttpStatus.NOT_FOUND));
        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) throws BusinessException {
        Customer customer = customerMapper.toEntity(dto);
        Customer saved = customerService.createCustomer(customer);
        return customerMapper.toDTO(saved);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) throws BusinessException {
        Customer newData = customerMapper.toEntity(dto);
        Customer updated = customerService.updateCustomer(id, newData);
        return customerMapper.toDTO(updated);
    }

    @Override
    public void deleteCustomer(Long id) throws BusinessException {
        customerService.deleteCustomer(id);
    }
}