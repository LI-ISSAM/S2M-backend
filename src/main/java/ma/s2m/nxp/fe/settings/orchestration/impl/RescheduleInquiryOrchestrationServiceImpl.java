package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.rescheduleinquiry.RescheduleInquiry;
import ma.s2m.nxp.fe.settings.dto.reschedule_inquiry.RescheduleInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.RescheduleInquiryMapper;
import ma.s2m.nxp.fe.settings.orchestration.IRescheduleInquiryOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IRescheduleInquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RescheduleInquiryOrchestrationServiceImpl implements IRescheduleInquiryOrchestrationService {

    private final IRescheduleInquiryService rescheduleInquiryService;
    private final RescheduleInquiryMapper rescheduleInquiryMapper;

    public RescheduleInquiryOrchestrationServiceImpl(IRescheduleInquiryService rescheduleInquiryService,
                                                     RescheduleInquiryMapper rescheduleInquiryMapper) {
        this.rescheduleInquiryService = rescheduleInquiryService;
        this.rescheduleInquiryMapper = rescheduleInquiryMapper;
    }

    @Override
    public RescheduleInquiriesPageResponse getAllRescheduleInquiries(int page, int limit, String cardNumber, String rnn) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<RescheduleInquiry> result = rescheduleInquiryService.getAllRescheduleInquiries(pageRequest, cardNumber, rnn);

        List<RescheduleInquiryDTO> content = result.getContent().stream()
                .map(rescheduleInquiryMapper::toDTO)
                .toList();

        return new RescheduleInquiriesPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    public List<RescheduleInquiryDTO> getAllRescheduleInquiriesForExport( String cardNumber, String rnn) {
        Page<RescheduleInquiry> result = rescheduleInquiryService.getAllRescheduleInquiries(Pageable.unpaged(),cardNumber,rnn);
        return result.getContent().stream()
                .map(rescheduleInquiryMapper::toDTO)
                .toList();
    }

    @Override
    public RescheduleInquiryDTO getRescheduleInquiryById(Long id) throws BusinessException {
        RescheduleInquiry rescheduleInquiry = rescheduleInquiryService.getRescheduleInquiryById(id)
                .orElseThrow(() -> new BusinessException("RSI_002", "Reschedule Inquiry not found", HttpStatus.NOT_FOUND));
        return rescheduleInquiryMapper.toDTO(rescheduleInquiry);
    }

    @Override
    public RescheduleInquiryDTO createRescheduleInquiry(RescheduleInquiryDTO dto) throws BusinessException {
        RescheduleInquiry rescheduleInquiry = rescheduleInquiryMapper.toEntity(dto);
        RescheduleInquiry saved = rescheduleInquiryService.createRescheduleInquiry(rescheduleInquiry, dto.getCardId());
        return rescheduleInquiryMapper.toDTO(saved);
    }

    @Override
    public RescheduleInquiryDTO updateRescheduleInquiry(Long id, RescheduleInquiryDTO dto) throws BusinessException {
        RescheduleInquiry newData = rescheduleInquiryMapper.toEntity(dto);
        RescheduleInquiry updated = rescheduleInquiryService.updateRescheduleInquiry(id, newData, dto.getCardId());
        return rescheduleInquiryMapper.toDTO(updated);
    }

    @Override
    public void deleteRescheduleInquiry(Long id) throws BusinessException {
        rescheduleInquiryService.deleteRescheduleInquiry(id);
    }
}