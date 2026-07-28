package ma.s2m.nxp.fe.settings.DTO.risk;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreditRiskScoreDTO {

    private Long customerId;

    /** 0 (très risqué) à 100 (excellent), calculé par règles — voir RiskScoringService. */
    private int creditScore;

    /** LOW / MEDIUM / HIGH, dérivé de creditScore. */
    private String riskBand;

    /** Probabilité heuristique (0-100) que la PROCHAINE échéance soit payée en retard. */
    private int nextInstallmentLatePaymentRisk;

    private String paymentHistorySummary; // ex: "8 échéances payées, 1 en retard sur 9 (11% de retard)"

    /** Facteurs ayant influencé le score (transparence/explicabilité). */
    private List<String> factors;

    /**
     * Explication en langage naturel générée par Claude à partir du score déjà
     * calculé — null si désactivé/échec (le score reste valide sans elle).
     */
    private String aiExplanation;
}