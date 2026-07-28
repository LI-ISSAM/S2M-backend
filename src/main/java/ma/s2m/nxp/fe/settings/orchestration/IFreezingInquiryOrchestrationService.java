package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.freezinginquiry.FreezingInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.FreezingInquiriesPageResponse;

public interface IFreezingInquiryOrchestrationService {

    FreezingInquiriesPageResponse getAllFreezingInquiries(int page, int limit, String cardNumber, String rnn);

    FreezingInquiryDTO getFreezingInquiryById(Long id) throws BusinessException;

    FreezingInquiryDTO createFreezingInquiry(FreezingInquiryDTO dto) throws BusinessException;

    FreezingInquiryDTO updateFreezingInquiry(Long id, FreezingInquiryDTO dto) throws BusinessException;

    void deleteFreezingInquiry(Long id) throws BusinessException;
}