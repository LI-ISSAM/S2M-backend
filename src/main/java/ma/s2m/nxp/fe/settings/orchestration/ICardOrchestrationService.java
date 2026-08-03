package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.dto.card.CardDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.CardsPageResponse;

import java.util.List;

public interface ICardOrchestrationService {

    CardsPageResponse getAllCards(int page, int limit, String cardNumber, String customerName);

    List<CardDTO> getAllCardsForExport(String cardNumber , String customerName);
    CardDTO getCardById(Long id) throws BusinessException;

    CardDTO createCard(CardDTO dto) throws BusinessException;

    CardDTO updateCard(Long id, CardDTO dto) throws BusinessException;

    void deleteCard(Long id) throws BusinessException;
}