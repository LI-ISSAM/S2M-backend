package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.freezing_inquiry.FreezingInquiry;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IFreezingInquiryService {

    Page<FreezingInquiry> getAllFreezingInquiries(Pageable pageable, String cardNumber, String rnn);

    Optional<FreezingInquiry> getFreezingInquiryById(Long id);

    FreezingInquiry createFreezingInquiry(FreezingInquiry freezingInquiry, Long cardId) throws BusinessException;

    FreezingInquiry updateFreezingInquiry(Long id, FreezingInquiry newData, Long cardId) throws BusinessException;

    void deleteFreezingInquiry(Long id) throws BusinessException;
}