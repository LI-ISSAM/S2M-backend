package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.force_closure_inquiry.ForceClosureInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.ForceClosureInquiriesPageResponse;

import java.util.List;

public interface IForceClosureInquiryOrchestrationService {

    ForceClosureInquiriesPageResponse getAllForceClosureInquiries(int page, int limit, String cardNumber, String rnn);

    List<ForceClosureInquiryDTO> getAllForceClosureInquiriesForExport(String cardNumber, String rnn);

    ForceClosureInquiryDTO getForceClosureInquiryById(Long id) throws BusinessException;

    ForceClosureInquiryDTO createForceClosureInquiry(ForceClosureInquiryDTO dto) throws BusinessException;

    ForceClosureInquiryDTO updateForceClosureInquiry(Long id, ForceClosureInquiryDTO dto) throws BusinessException;

    void deleteForceClosureInquiry(Long id) throws BusinessException;
}