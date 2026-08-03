package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.dto.offer.OfferDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.OfferMapper;
import ma.s2m.nxp.fe.settings.orchestration.IOfferOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IOfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferOrchestrationServiceImpl implements IOfferOrchestrationService {

    private final IOfferService offerService;
    private final OfferMapper offerMapper;

    public OfferOrchestrationServiceImpl(IOfferService offerService, OfferMapper offerMapper) {
        this.offerService = offerService;
        this.offerMapper = offerMapper;
    }

    @Override
    public OffersPageResponse getAllOffers(int page, int limit, String name, Long programId) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "startDate"));

        Page<Offer> result = offerService.getAllOffers(pageRequest, name, programId);

        List<OfferDTO> content = result.getContent().stream()
                .map(offerMapper::toDTO)
                .toList();

        return new OffersPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    public List<OfferDTO> getAllOffersForExport(String name , Long programId){
        Page<Offer> result = offerService.getAllOffers(Pageable.unpaged(),name,programId);
        return result.getContent().stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    @Override
    public OfferDTO getOfferById(Long id) throws BusinessException {
        Offer offer = offerService.getOfferById(id)
                .orElseThrow(() -> new BusinessException("OFR_002", "Offer not found", HttpStatus.NOT_FOUND));
        return offerMapper.toDTO(offer);
    }

    @Override
    public OfferDTO createOffer(OfferDTO dto) throws BusinessException {
        Offer offer = offerMapper.toEntity(dto);
        Offer saved = offerService.createOffer(offer, dto.getProgramId());
        return offerMapper.toDTO(saved);
    }

    @Override
    public OfferDTO updateOffer(Long id, OfferDTO dto) throws BusinessException {
        Offer newData = offerMapper.toEntity(dto);
        Offer updated = offerService.updateOffer(id, newData, dto.getProgramId());
        return offerMapper.toDTO(updated);
    }

    @Override
    public void deleteOffer(Long id) throws BusinessException {
        offerService.deleteOffer(id);
    }
}