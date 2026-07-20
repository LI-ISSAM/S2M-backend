package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.installment.CustomerInstallmentDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.ICustomerInstallmentOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.CustomerInstallmentsPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exposé sur /api/v1/installments pour matcher InstallmentService.js tel quel
 * (customerName, customerEmail, customerId — sans suffixe _like, mais on fait
 * quand même une recherche "contains" pour rester utile côté UI).
 */
@RestController
@RequestMapping("/api/v1/installments")
public class CustomerInstallmentController {

    private final ICustomerInstallmentOrchestrationService installmentOrchestrationService;

    public CustomerInstallmentController(ICustomerInstallmentOrchestrationService installmentOrchestrationService) {
        this.installmentOrchestrationService = installmentOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllInstallments(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "customerEmail", required = false) String customerEmail,
            @RequestParam(value = "customerId", required = false) Long customerId) {

        CustomerInstallmentsPageResponse result = installmentOrchestrationService.getAllInstallments(
                page, limit, customerName, customerEmail, customerId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInstallmentDTO> getInstallmentById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(installmentOrchestrationService.getInstallmentById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerInstallmentDTO> createInstallment(@Valid @RequestBody CustomerInstallmentDTO dto)
            throws BusinessException {
        CustomerInstallmentDTO created = installmentOrchestrationService.createInstallment(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerInstallmentDTO> updateInstallment(@PathVariable Long id,
                                                                    @Valid @RequestBody CustomerInstallmentDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(installmentOrchestrationService.updateInstallment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallment(@PathVariable Long id) throws BusinessException {
        installmentOrchestrationService.deleteInstallment(id);
        return ResponseEntity.noContent().build();
    }
}