package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.member.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long>,
        JpaSpecificationExecutor<Institution> {

    Optional<Institution> findByReference(String reference);

    boolean existsByReference(String reference);

    boolean existsByNameIgnoreCase(String name);
}