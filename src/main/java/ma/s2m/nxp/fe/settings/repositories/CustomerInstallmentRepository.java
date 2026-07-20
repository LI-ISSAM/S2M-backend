package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInstallmentRepository extends JpaRepository<CustomerInstallment, Long>,
        JpaSpecificationExecutor<CustomerInstallment> {

    boolean existsByCustomer_Id(Long customerId);

    List<CustomerInstallment> findAllByCustomer_Id(Long customerId);
}