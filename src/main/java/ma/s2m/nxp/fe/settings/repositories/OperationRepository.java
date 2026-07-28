package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>,
        JpaSpecificationExecutor<Operation> {

    boolean existsByMerchant_Id(Long merchantId);

    boolean existsByBnplProgram_Id(Long programId);

    boolean existsByCustomer_Id(Long customerId);
}