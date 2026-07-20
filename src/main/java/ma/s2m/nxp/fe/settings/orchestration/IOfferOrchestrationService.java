package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.offer.OfferDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.OffersPageResponse;

public interface IOfferOrchestrationService {

    OffersPageResponse getAllOffers(int page, int limit, String name, Long programId);

    OfferDTO getOfferById(Long id) throws BusinessException;

    OfferDTO createOffer(OfferDTO dto) throws BusinessException;

    OfferDTO updateOffer(Long id, OfferDTO dto) throws BusinessException;

    void deleteOffer(Long id) throws BusinessException;
}