package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>,
        JpaSpecificationExecutor<Merchant> {

    boolean existsByReferenceIgnoreCase(String reference);
    boolean existsByMccCode(String mccCode);

    boolean existsByReferenceIgnoreCaseAndIdNot(String reference, Long id);

    boolean existsByInstitution_Id(Long institutionId);
}