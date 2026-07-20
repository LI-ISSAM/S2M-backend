package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.card.CardDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.CardsPageResponse;

public interface ICardOrchestrationService {

    CardsPageResponse getAllCards(int page, int limit, String cardNumber, String customerName);

    CardDTO getCardById(Long id) throws BusinessException;

    CardDTO createCard(CardDTO dto) throws BusinessException;

    CardDTO updateCard(Long id, CardDTO dto) throws BusinessException;

    void deleteCard(Long id) throws BusinessException;
}