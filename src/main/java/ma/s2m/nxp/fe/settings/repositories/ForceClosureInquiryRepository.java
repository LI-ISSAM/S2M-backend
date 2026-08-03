package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.force_closure_inquiry.ForceClosureInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ForceClosureInquiryRepository extends JpaRepository<ForceClosureInquiry, Long>,
        JpaSpecificationExecutor<ForceClosureInquiry> {

    boolean existsByCard_Id(Long cardId);

    boolean existsByCard_IdAndIdNot(Long cardId, Long id);
}