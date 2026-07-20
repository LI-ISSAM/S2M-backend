package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>,
        JpaSpecificationExecutor<Offer> {

    List<Offer> findAllByProgram_IdAndIsDefaultTrue(Long programId);
    boolean existsByName(String name);

    boolean existsByProgram_Id(Long programId);
}