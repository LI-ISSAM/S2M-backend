package ma.s2m.nxp.fe.settings.controllers;

import ma.s2m.nxp.fe.settings.DTO.programrecommendation.ProgramRecommendationResponseDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.services.IProgramRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/programs/recommendations")
public class ProgramRecommendationController {

    private final IProgramRecommendationService programRecommendationService;

    public ProgramRecommendationController(IProgramRecommendationService programRecommendationService) {
        this.programRecommendationService = programRecommendationService;
    }

    @GetMapping
    public ResponseEntity<ProgramRecommendationResponseDTO> recommend(
            @RequestParam Long customerId,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(defaultValue = "true") boolean ai) throws BusinessException {
        return ResponseEntity.ok(programRecommendationService.recommend(customerId, amount, ai));
    }
}