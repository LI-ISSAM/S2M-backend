package ma.s2m.nxp.fe.settings.dto.program_recommendation;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EligibleProgramDTO {

    private Long programId;
    private String programName;
    private String programType;

    /** Score de pertinence normalisé sur 100, calculé sans IA (voir ProgramScoringService). */
    private double score;

    private String feeSummary;      // ex: "PERCENTAGE 2.5%" ou "FIXED 15.00"
    private BigDecimal maxAmountPerTransaction;
    private BigDecimal maxMonthlyInstallment;

    /** Explique pourquoi ce programme a été retenu (âge OK, salaire OK, subBin OK...). */
    private List<String> matchedCriteria;
}