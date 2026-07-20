package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.card.CardDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.ICardOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.CardsPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final ICardOrchestrationService cardOrchestrationService;

    public CardController(ICardOrchestrationService cardOrchestrationService) {
        this.cardOrchestrationService = cardOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, cardNumber_like,
     * customerName_like (jointure vers Customer.fullName).
     */
    @GetMapping
    public ResponseEntity<Object> getAllCards(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "cardNumber_like", required = false) String cardNumber,
            @RequestParam(value = "customerName_like", required = false) String customerName) {

        CardsPageResponse result = cardOrchestrationService.getAllCards(page, limit, cardNumber, customerName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(cardOrchestrationService.getCardById(id));
    }

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO)
            throws BusinessException {
        CardDTO created = cardOrchestrationService.createCard(cardDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable Long id,
                                              @Valid @RequestBody CardDTO cardDTO)
            throws BusinessException {
        return ResponseEntity.ok(cardOrchestrationService.updateCard(id, cardDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) throws BusinessException {
        cardOrchestrationService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}