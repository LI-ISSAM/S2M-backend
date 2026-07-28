package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.installmentplan.InstallmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentPlanRepository extends JpaRepository<InstallmentPlan, Long>,
        JpaSpecificationExecutor<InstallmentPlan> {

    boolean existsByCustomer_Id(Long customerId);

    boolean existsByOffer_Id(Long offerId);

    Optional<InstallmentPlan> findFirstByCustomer_IdOrderByStartDateDesc(Long customerId);

    List<InstallmentPlan> findAllByCustomer_Id(Long customerId);
}