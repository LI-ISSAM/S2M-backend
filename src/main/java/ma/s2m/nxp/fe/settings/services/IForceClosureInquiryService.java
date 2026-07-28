package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.forceclosureinquiry.ForceClosureInquiry;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IForceClosureInquiryService {

    Page<ForceClosureInquiry> getAllForceClosureInquiries(Pageable pageable, String cardNumber, String rnn);

    Optional<ForceClosureInquiry> getForceClosureInquiryById(Long id);

    ForceClosureInquiry createForceClosureInquiry(ForceClosureInquiry forceClosureInquiry, Long cardId) throws BusinessException;

    ForceClosureInquiry updateForceClosureInquiry(Long id, ForceClosureInquiry newData, Long cardId) throws BusinessException;

    void deleteForceClosureInquiry(Long id) throws BusinessException;
}