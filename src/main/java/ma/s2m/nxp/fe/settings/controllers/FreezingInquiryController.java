package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.freezinginquiry.FreezingInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IFreezingInquiryOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.FreezingInquiriesPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Compatible avec FreezingInquiryService.js : _page, _limit, cardNumber_like
 * (jointure vers Card.cardNumber), rnn_like.
 */
@RestController
@RequestMapping("/api/v1/freezingInquiries")
public class FreezingInquiryController {

    private final IFreezingInquiryOrchestrationService freezingInquiryOrchestrationService;

    public FreezingInquiryController(IFreezingInquiryOrchestrationService freezingInquiryOrchestrationService) {
        this.freezingInquiryOrchestrationService = freezingInquiryOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllFreezingInquiries(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "cardNumber_like", required = false) String cardNumber,
            @RequestParam(value = "rnn_like", required = false) String rnn) {

        FreezingInquiriesPageResponse result =
                freezingInquiryOrchestrationService.getAllFreezingInquiries(page, limit, cardNumber, rnn);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FreezingInquiryDTO> getFreezingInquiryById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(freezingInquiryOrchestrationService.getFreezingInquiryById(id));
    }

    @PostMapping
    public ResponseEntity<FreezingInquiryDTO> createFreezingInquiry(@Valid @RequestBody FreezingInquiryDTO dto)
            throws BusinessException {
        FreezingInquiryDTO created = freezingInquiryOrchestrationService.createFreezingInquiry(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FreezingInquiryDTO> updateFreezingInquiry(@PathVariable Long id,
                                                                    @Valid @RequestBody FreezingInquiryDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(freezingInquiryOrchestrationService.updateFreezingInquiry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFreezingInquiry(@PathVariable Long id) throws BusinessException {
        freezingInquiryOrchestrationService.deleteFreezingInquiry(id);
        return ResponseEntity.noContent().build();
    }
}