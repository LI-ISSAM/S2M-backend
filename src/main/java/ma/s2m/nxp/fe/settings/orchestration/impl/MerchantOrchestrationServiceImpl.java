package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.DTO.merchant.MerchantDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.MerchantMapper;
import ma.s2m.nxp.fe.settings.orchestration.IMerchantOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IMerchantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantOrchestrationServiceImpl implements IMerchantOrchestrationService {

    private final IMerchantService merchantService;
    private final MerchantMapper merchantMapper;

    public MerchantOrchestrationServiceImpl(IMerchantService merchantService, MerchantMapper merchantMapper) {
        this.merchantService = merchantService;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public MerchantsPageResponse getAllMerchants(int page, int limit, String name, String reference, Long institutionId) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.ASC, "name"));

        Page<Merchant> result = merchantService.getAllMerchants(pageRequest, name, reference, institutionId);

        List<MerchantDTO> content = result.getContent().stream()
                .map(merchantMapper::toDTO)
                .toList();

        return new MerchantsPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    @Override
    public MerchantDTO getMerchantById(Long id) throws BusinessException {
        Merchant merchant = merchantService.getMerchantById(id)
                .orElseThrow(() -> new BusinessException("MER_002", "Merchant not found", HttpStatus.NOT_FOUND));
        return merchantMapper.toDTO(merchant);
    }

    @Override
    public MerchantDTO createMerchant(MerchantDTO dto) throws BusinessException {
        Merchant merchant = merchantMapper.toEntity(dto);
        Merchant saved = merchantService.createMerchant(merchant, dto.getInstitutionId());
        return merchantMapper.toDTO(saved);
    }

    @Override
    public MerchantDTO updateMerchant(Long id, MerchantDTO dto) throws BusinessException {
        Merchant newData = merchantMapper.toEntity(dto);
        Merchant updated = merchantService.updateMerchant(id, newData, dto.getInstitutionId());
        return merchantMapper.toDTO(updated);
    }

    @Override
    public void deleteMerchant(Long id) throws BusinessException {
        merchantService.deleteMerchant(id);
    }
}