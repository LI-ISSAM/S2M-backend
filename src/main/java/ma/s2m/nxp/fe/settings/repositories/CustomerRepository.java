package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {

    boolean existsByContact_EmailIgnoreCase(String email);

    boolean existsByContact_EmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByCustomerId(String customerId);
}