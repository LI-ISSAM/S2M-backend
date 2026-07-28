package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.forceclosureinquiry.ForceClosureInquiry;
import ma.s2m.nxp.fe.settings.dto.forceclosureinquiry.ForceClosureInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.ForceClosureInquiryMapper;
import ma.s2m.nxp.fe.settings.orchestration.IForceClosureInquiryOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IForceClosureInquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForceClosureInquiryOrchestrationServiceImpl implements IForceClosureInquiryOrchestrationService {

    private final IForceClosureInquiryService forceClosureInquiryService;
    private final ForceClosureInquiryMapper forceClosureInquiryMapper;

    public ForceClosureInquiryOrchestrationServiceImpl(IForceClosureInquiryService forceClosureInquiryService,
                                                       ForceClosureInquiryMapper forceClosureInquiryMapper) {
        this.forceClosureInquiryService = forceClosureInquiryService;
        this.forceClosureInquiryMapper = forceClosureInquiryMapper;
    }

    @Override
    public ForceClosureInquiriesPageResponse getAllForceClosureInquiries(int page, int limit, String cardNumber, String rnn) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ForceClosureInquiry> result = forceClosureInquiryService.getAllForceClosureInquiries(pageRequest, cardNumber, rnn);

        List<ForceClosureInquiryDTO> content = result.getContent().stream()
                .map(forceClosureInquiryMapper::toDTO)
                .toList();

        return new ForceClosureInquiriesPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public ForceClosureInquiryDTO getForceClosureInquiryById(Long id) throws BusinessException {
        ForceClosureInquiry forceClosureInquiry = forceClosureInquiryService.getForceClosureInquiryById(id)
                .orElseThrow(() -> new BusinessException("FCI_002", "Force Closure Inquiry not found", HttpStatus.NOT_FOUND));
        return forceClosureInquiryMapper.toDTO(forceClosureInquiry);
    }

    @Override
    public ForceClosureInquiryDTO createForceClosureInquiry(ForceClosureInquiryDTO dto) throws BusinessException {
        ForceClosureInquiry forceClosureInquiry = forceClosureInquiryMapper.toEntity(dto);
        ForceClosureInquiry saved = forceClosureInquiryService.createForceClosureInquiry(forceClosureInquiry, dto.getCardId());
        return forceClosureInquiryMapper.toDTO(saved);
    }

    @Override
    public ForceClosureInquiryDTO updateForceClosureInquiry(Long id, ForceClosureInquiryDTO dto) throws BusinessException {
        ForceClosureInquiry newData = forceClosureInquiryMapper.toEntity(dto);
        ForceClosureInquiry updated = forceClosureInquiryService.updateForceClosureInquiry(id, newData, dto.getCardId());
        return forceClosureInquiryMapper.toDTO(updated);
    }

    @Override
    public void deleteForceClosureInquiry(Long id) throws BusinessException {
        forceClosureInquiryService.deleteForceClosureInquiry(id);
    }
}