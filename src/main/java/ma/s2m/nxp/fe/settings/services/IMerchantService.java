package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IMerchantService {

    Page<Merchant> getAllMerchants(Pageable pageable, String name, String reference, Long institutionId);

    Optional<Merchant> getMerchantById(Long id);

    Merchant createMerchant(Merchant merchant, Long institutionId) throws BusinessException;

    Merchant updateMerchant(Long id, Merchant newData, Long institutionId) throws BusinessException;

    void deleteMerchant(Long id) throws BusinessException;
}