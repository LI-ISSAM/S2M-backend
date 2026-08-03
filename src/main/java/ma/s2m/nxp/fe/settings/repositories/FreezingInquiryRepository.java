package ma.s2m.nxp.fe.settings.repositories;

import ma.s2m.nxp.fe.settings.domain.freezing_inquiry.FreezingInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FreezingInquiryRepository extends JpaRepository<FreezingInquiry, Long>,
        JpaSpecificationExecutor<FreezingInquiry> {

    boolean existsByCard_Id(Long cardId);

    boolean existsByCard_IdAndIdNot(Long cardId, Long id);
}