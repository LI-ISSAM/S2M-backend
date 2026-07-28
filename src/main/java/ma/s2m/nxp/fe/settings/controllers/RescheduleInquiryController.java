package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.rescheduleinquiry.RescheduleInquiryDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IRescheduleInquiryOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.RescheduleInquiriesPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Compatible avec RescheduleInquiryService.js : _page, _limit,
 * cardNumber_like (jointure vers Card.cardNumber), rnn_like.
 */
@RestController
@RequestMapping("/api/v1/rescheduleInquiries")
public class RescheduleInquiryController {

    private final IRescheduleInquiryOrchestrationService rescheduleInquiryOrchestrationService;

    public RescheduleInquiryController(IRescheduleInquiryOrchestrationService rescheduleInquiryOrchestrationService) {
        this.rescheduleInquiryOrchestrationService = rescheduleInquiryOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllRescheduleInquiries(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "cardNumber_like", required = false) String cardNumber,
            @RequestParam(value = "rnn_like", required = false) String rnn) {

        RescheduleInquiriesPageResponse result =
                rescheduleInquiryOrchestrationService.getAllRescheduleInquiries(page, limit, cardNumber, rnn);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RescheduleInquiryDTO> getRescheduleInquiryById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(rescheduleInquiryOrchestrationService.getRescheduleInquiryById(id));
    }

    @PostMapping
    public ResponseEntity<RescheduleInquiryDTO> createRescheduleInquiry(@Valid @RequestBody RescheduleInquiryDTO dto)
            throws BusinessException {
        RescheduleInquiryDTO created = rescheduleInquiryOrchestrationService.createRescheduleInquiry(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RescheduleInquiryDTO> updateRescheduleInquiry(@PathVariable Long id,
                                                                        @Valid @RequestBody RescheduleInquiryDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(rescheduleInquiryOrchestrationService.updateRescheduleInquiry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRescheduleInquiry(@PathVariable Long id) throws BusinessException {
        rescheduleInquiryOrchestrationService.deleteRescheduleInquiry(id);
        return ResponseEntity.noContent().build();
    }
}