package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.reschedule_inquiry.RescheduleInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.RescheduleInquiriesPageResponse;

import java.util.List;

public interface IRescheduleInquiryOrchestrationService {

    RescheduleInquiriesPageResponse getAllRescheduleInquiries(int page, int limit, String cardNumber, String rnn);

    List<RescheduleInquiryDTO> getAllRescheduleInquiriesForExport(String cardNumber, String rnn);

    RescheduleInquiryDTO getRescheduleInquiryById(Long id) throws BusinessException;

    RescheduleInquiryDTO createRescheduleInquiry(RescheduleInquiryDTO dto) throws BusinessException;

    RescheduleInquiryDTO updateRescheduleInquiry(Long id, RescheduleInquiryDTO dto) throws BusinessException;

    void deleteRescheduleInquiry(Long id) throws BusinessException;
}