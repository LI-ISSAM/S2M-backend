package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.offer.OfferDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IOfferOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.OffersPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final IOfferOrchestrationService offerOrchestrationService;

    public OfferController(IOfferOrchestrationService offerOrchestrationService) {
        this.offerOrchestrationService = offerOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, name_like, programId,
     * réponse paginée avec header X-Total-Count.
     * Note : getDefaultOffer() côté frontend filtre côté client sur isDefault
     * après avoir appelé cet endpoint avec un grand _limit — aucune route
     * dédiée n'est donc nécessaire pour ce cas.
     */
    @GetMapping
    public ResponseEntity<Object> getAllOffers(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "programId", required = false) Long programId) {

        OffersPageResponse result = offerOrchestrationService.getAllOffers(page, limit, name, programId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(offerOrchestrationService.getOfferById(id));
    }

    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferDTO offerDTO)
            throws BusinessException {
        OfferDTO created = offerOrchestrationService.createOffer(offerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id,
                                                @Valid @RequestBody OfferDTO offerDTO)
            throws BusinessException {
        return ResponseEntity.ok(offerOrchestrationService.updateOffer(id, offerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) throws BusinessException {
        offerOrchestrationService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}