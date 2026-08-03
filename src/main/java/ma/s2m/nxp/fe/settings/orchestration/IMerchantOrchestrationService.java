package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.merchant.MerchantDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.MerchantsPageResponse;

import java.util.List;

public interface IMerchantOrchestrationService {

    MerchantsPageResponse getAllMerchants(int page, int limit, String name, String reference, Long institutionId);
    List<MerchantDTO> getAllMerchantsForExport(String name, String reference , Long institutionId);
    MerchantDTO getMerchantById(Long id) throws BusinessException;

    MerchantDTO createMerchant(MerchantDTO dto) throws BusinessException;

    MerchantDTO updateMerchant(Long id, MerchantDTO dto) throws BusinessException;

    void deleteMerchant(Long id) throws BusinessException;
}