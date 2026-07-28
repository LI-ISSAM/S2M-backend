package ma.s2m.nxp.fe.settings.controllers;

import ma.s2m.nxp.fe.settings.DTO.risk.CreditRiskScoreDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.services.IRiskAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/risk-score")
public class RiskAssessmentController {

    private final IRiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(IRiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping
    public ResponseEntity<CreditRiskScoreDTO> getRiskScore(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "true") boolean ai) throws BusinessException {
        return ResponseEntity.ok(riskAssessmentService.assessRisk(customerId, ai));
    }
}