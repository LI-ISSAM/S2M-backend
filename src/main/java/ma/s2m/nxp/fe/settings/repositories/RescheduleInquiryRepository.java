package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.rescheduleinquiry.RescheduleInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RescheduleInquiryRepository extends JpaRepository<RescheduleInquiry, Long>,
        JpaSpecificationExecutor<RescheduleInquiry> {

    boolean existsByCard_Id(Long cardId);

    boolean existsByCard_IdAndIdNot(Long cardId, Long id);
}