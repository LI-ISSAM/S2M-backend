package ma.s2m.nxp.fe.settings.dto.program_recommendation;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProgramRecommendationResponseDTO {

    private Long customerId;
    private BigDecimal transactionAmount; // optionnel, si fourni dans la requête

    /** Programmes éligibles, triés du meilleur score au moins bon. */
    private List<EligibleProgramDTO> eligiblePrograms;

    private Long recommendedProgramId;

    /**
     * Explication en langage naturel générée par Claude, basée UNIQUEMENT sur
     * les scores/critères déjà calculés (pas de chiffres inventés). Peut être
     * null si l'appel IA a échoué ou est désactivé — le classement par score
     * reste valide dans tous les cas.
     */
    private String aiExplanation;
}