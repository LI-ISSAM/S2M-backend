package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.freezinginquiry.FreezingInquiry;
import ma.s2m.nxp.fe.settings.DTO.freezinginquiry.FreezingInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.FreezingInquiryMapper;
import ma.s2m.nxp.fe.settings.orchestration.IFreezingInquiryOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IFreezingInquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreezingInquiryOrchestrationServiceImpl implements IFreezingInquiryOrchestrationService {

    private final IFreezingInquiryService freezingInquiryService;
    private final FreezingInquiryMapper freezingInquiryMapper;

    public FreezingInquiryOrchestrationServiceImpl(IFreezingInquiryService freezingInquiryService,
                                                   FreezingInquiryMapper freezingInquiryMapper) {
        this.freezingInquiryService = freezingInquiryService;
        this.freezingInquiryMapper = freezingInquiryMapper;
    }

    @Override
    public FreezingInquiriesPageResponse getAllFreezingInquiries(int page, int limit, String cardNumber, String rnn) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<FreezingInquiry> result = freezingInquiryService.getAllFreezingInquiries(pageRequest, cardNumber, rnn);

        List<FreezingInquiryDTO> content = result.getContent().stream()
                .map(freezingInquiryMapper::toDTO)
                .toList();

        return new FreezingInquiriesPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public FreezingInquiryDTO getFreezingInquiryById(Long id) throws BusinessException {
        FreezingInquiry freezingInquiry = freezingInquiryService.getFreezingInquiryById(id)
                .orElseThrow(() -> new BusinessException("FZI_002", "Freezing Inquiry not found", HttpStatus.NOT_FOUND));
        return freezingInquiryMapper.toDTO(freezingInquiry);
    }

    @Override
    public FreezingInquiryDTO createFreezingInquiry(FreezingInquiryDTO dto) throws BusinessException {
        FreezingInquiry freezingInquiry = freezingInquiryMapper.toEntity(dto);
        FreezingInquiry saved = freezingInquiryService.createFreezingInquiry(freezingInquiry, dto.getCardId());
        return freezingInquiryMapper.toDTO(saved);
    }

    @Override
    public FreezingInquiryDTO updateFreezingInquiry(Long id, FreezingInquiryDTO dto) throws BusinessException {
        FreezingInquiry newData = freezingInquiryMapper.toEntity(dto);
        FreezingInquiry updated = freezingInquiryService.updateFreezingInquiry(id, newData, dto.getCardId());
        return freezingInquiryMapper.toDTO(updated);
    }

    @Override
    public void deleteFreezingInquiry(Long id) throws BusinessException {
        freezingInquiryService.deleteFreezingInquiry(id);
    }
}