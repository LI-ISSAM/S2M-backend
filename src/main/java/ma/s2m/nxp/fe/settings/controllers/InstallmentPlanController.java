package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.installmentplan.InstallmentPlanDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IInstallmentPlanOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.InstallmentPlansPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/installmentPlans")
public class InstallmentPlanController {

    private final IInstallmentPlanOrchestrationService installmentPlanOrchestrationService;

    public InstallmentPlanController(IInstallmentPlanOrchestrationService installmentPlanOrchestrationService) {
        this.installmentPlanOrchestrationService = installmentPlanOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, customerName_like
     * (jointure vers Customer.fullName), offerName_like (jointure vers Offer.name).
     */
    @GetMapping
    public ResponseEntity<Object> getAllInstallmentPlans(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "customerName_like", required = false) String customerName,
            @RequestParam(value = "offerName_like", required = false) String offerName,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "offerId", required = false) Long offerId) {

        InstallmentPlansPageResponse result = installmentPlanOrchestrationService.getAllInstallmentPlans(
                page, limit, customerName, offerName, customerId, offerId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallmentPlanDTO> getInstallmentPlanById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(installmentPlanOrchestrationService.getInstallmentPlanById(id));
    }

    @PostMapping
    public ResponseEntity<InstallmentPlanDTO> createInstallmentPlan(@Valid @RequestBody InstallmentPlanDTO dto)
            throws BusinessException {
        InstallmentPlanDTO created = installmentPlanOrchestrationService.createInstallmentPlan(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstallmentPlanDTO> updateInstallmentPlan(@PathVariable Long id,
                                                                    @Valid @RequestBody InstallmentPlanDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(installmentPlanOrchestrationService.updateInstallmentPlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallmentPlan(@PathVariable Long id) throws BusinessException {
        installmentPlanOrchestrationService.deleteInstallmentPlan(id);
        return ResponseEntity.noContent().build();
    }
}