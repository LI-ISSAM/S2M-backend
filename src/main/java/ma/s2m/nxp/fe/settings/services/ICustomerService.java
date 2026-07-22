package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICustomerService {

    Page<Customer> getAllCustomers(Pageable pageable, String lastName,String email );

    Optional<Customer> getCustomerById(Long id);

    Customer createCustomer(Customer customer) throws BusinessException;

    Customer updateCustomer(Long id, Customer newData) throws BusinessException;

    void deleteCustomer(Long id) throws BusinessException;
}