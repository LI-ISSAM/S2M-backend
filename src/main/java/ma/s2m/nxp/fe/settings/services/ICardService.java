package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICardService {

    Page<Card> getAllCards(Pageable pageable, String cardNumber, String customerName);

    Optional<Card> getCardById(Long id);

    Card createCard(Card card, Long customerId, Long programId) throws BusinessException;

    Card updateCard(Long id, Card newData, Long customerId, Long programId) throws BusinessException;

    void deleteCard(Long id) throws BusinessException;
}