package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IOfferService {

    Page<Offer> getAllOffers(Pageable pageable, String name, Long programId);

    Optional<Offer> getOfferById(Long id);

    Offer createOffer(Offer offer, Long programId) throws BusinessException;

    Offer updateOffer(Long id, Offer newData, Long programId) throws BusinessException;

    void deleteOffer(Long id) throws BusinessException;
}