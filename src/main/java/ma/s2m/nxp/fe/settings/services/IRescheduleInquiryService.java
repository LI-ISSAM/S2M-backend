package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.rescheduleinquiry.RescheduleInquiry;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRescheduleInquiryService {

    Page<RescheduleInquiry> getAllRescheduleInquiries(Pageable pageable, String cardNumber, String rnn);

    Optional<RescheduleInquiry> getRescheduleInquiryById(Long id);

    RescheduleInquiry createRescheduleInquiry(RescheduleInquiry rescheduleInquiry, Long cardId) throws BusinessException;

    RescheduleInquiry updateRescheduleInquiry(Long id, RescheduleInquiry newData, Long cardId) throws BusinessException;

    void deleteRescheduleInquiry(Long id) throws BusinessException;
}