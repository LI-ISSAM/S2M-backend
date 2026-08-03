package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.dto.card.CardDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.CardMapper;
import ma.s2m.nxp.fe.settings.orchestration.ICardOrchestrationService;
import ma.s2m.nxp.fe.settings.services.ICardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardOrchestrationServiceImpl implements ICardOrchestrationService {

    private final ICardService cardService;
    private final CardMapper cardMapper;

    public CardOrchestrationServiceImpl(ICardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardsPageResponse getAllCards(int page, int limit, String cardNumber, String customerName) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Card> result = cardService.getAllCards(pageRequest, cardNumber, customerName);

        List<CardDTO> content = result.getContent().stream()
                .map(cardMapper::toDTO)
                .toList();

        return new CardsPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    public List<CardDTO> getAllCardsForExport(String cardNumber , String customerName){
        Page<Card> result = cardService.getAllCards(Pageable.unpaged(), cardNumber,customerName);
        return result.getContent().stream()
                .map(cardMapper::toDTO)
                .toList();
    }

    @Override
    public CardDTO getCardById(Long id) throws BusinessException {
        Card card = cardService.getCardById(id)
                .orElseThrow(() -> new BusinessException("CRD_002", "Card not found", HttpStatus.NOT_FOUND));
        return cardMapper.toDTO(card);
    }

    @Override
    public CardDTO createCard(CardDTO dto) throws BusinessException {
        Card card = cardMapper.toEntity(dto);
        Card saved = cardService.createCard(card, dto.getCustomerId(), dto.getProgramId());
        return cardMapper.toDTO(saved);
    }

    @Override
    public CardDTO updateCard(Long id, CardDTO dto) throws BusinessException {
        Card newData = cardMapper.toEntity(dto);
        Card updated = cardService.updateCard(id, newData, dto.getCustomerId(), dto.getProgramId());
        return cardMapper.toDTO(updated);
    }

    @Override
    public void deleteCard(Long id) throws BusinessException {
        cardService.deleteCard(id);
    }
}