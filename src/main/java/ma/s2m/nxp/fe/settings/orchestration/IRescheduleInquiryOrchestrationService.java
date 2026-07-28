package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.rescheduleinquiry.RescheduleInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.RescheduleInquiriesPageResponse;

public interface IRescheduleInquiryOrchestrationService {

    RescheduleInquiriesPageResponse getAllRescheduleInquiries(int page, int limit, String cardNumber, String rnn);

    RescheduleInquiryDTO getRescheduleInquiryById(Long id) throws BusinessException;

    RescheduleInquiryDTO createRescheduleInquiry(RescheduleInquiryDTO dto) throws BusinessException;

    RescheduleInquiryDTO updateRescheduleInquiry(Long id, RescheduleInquiryDTO dto) throws BusinessException;

    void deleteRescheduleInquiry(Long id) throws BusinessException;
}