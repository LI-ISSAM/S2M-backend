package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.operation.OperationDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IOperationOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.OperationsPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Compatible avec OperationService.js : _page, _limit, reference_like
 * (recherche par référence Merchant), email_like (recherche par email
 * Customer), programName_like (recherche par nom de Program).
 */
@RestController
@RequestMapping("/api/v1/operations")
public class OperationController {

    private final IOperationOrchestrationService operationOrchestrationService;

    public OperationController(IOperationOrchestrationService operationOrchestrationService) {
        this.operationOrchestrationService = operationOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllOperations(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "programName_like", required = false) String programName) {

        OperationsPageResponse result = operationOrchestrationService.getAllOperations(page, limit, reference, email, programName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDTO> getOperationById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(operationOrchestrationService.getOperationById(id));
    }

    @PostMapping
    public ResponseEntity<OperationDTO> createOperation(@Valid @RequestBody OperationDTO dto) throws BusinessException {
        OperationDTO created = operationOrchestrationService.createOperation(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperationDTO> updateOperation(@PathVariable Long id, @Valid @RequestBody OperationDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(operationOrchestrationService.updateOperation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) throws BusinessException {
        operationOrchestrationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }
}