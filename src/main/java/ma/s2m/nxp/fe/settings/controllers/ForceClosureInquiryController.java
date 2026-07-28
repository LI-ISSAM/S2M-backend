package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.forceclosureinquiry.ForceClosureInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IForceClosureInquiryOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.ForceClosureInquiriesPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Compatible avec ForceClosureInquiryService.js : _page, _limit,
 * cardNumber_like (jointure vers Card.cardNumber), rnn_like.
 */
@RestController
@RequestMapping("/api/v1/forceClosureInquiries")
public class ForceClosureInquiryController {

    private final IForceClosureInquiryOrchestrationService forceClosureInquiryOrchestrationService;

    public ForceClosureInquiryController(IForceClosureInquiryOrchestrationService forceClosureInquiryOrchestrationService) {
        this.forceClosureInquiryOrchestrationService = forceClosureInquiryOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllForceClosureInquiries(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "cardNumber_like", required = false) String cardNumber,
            @RequestParam(value = "rnn_like", required = false) String rnn) {

        ForceClosureInquiriesPageResponse result =
                forceClosureInquiryOrchestrationService.getAllForceClosureInquiries(page, limit, cardNumber, rnn);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForceClosureInquiryDTO> getForceClosureInquiryById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(forceClosureInquiryOrchestrationService.getForceClosureInquiryById(id));
    }

    @PostMapping
    public ResponseEntity<ForceClosureInquiryDTO> createForceClosureInquiry(@Valid @RequestBody ForceClosureInquiryDTO dto)
            throws BusinessException {
        ForceClosureInquiryDTO created = forceClosureInquiryOrchestrationService.createForceClosureInquiry(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ForceClosureInquiryDTO> updateForceClosureInquiry(@PathVariable Long id,
                                                                            @Valid @RequestBody ForceClosureInquiryDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(forceClosureInquiryOrchestrationService.updateForceClosureInquiry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForceClosureInquiry(@PathVariable Long id) throws BusinessException {
        forceClosureInquiryOrchestrationService.deleteForceClosureInquiry(id);
        return ResponseEntity.noContent().build();
    }
}