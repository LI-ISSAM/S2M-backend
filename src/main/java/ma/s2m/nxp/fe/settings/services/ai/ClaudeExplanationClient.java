package ma.s2m.nxp.fe.settings.services.ai;

import ma.s2m.nxp.fe.settings.DTO.programrecommendation.EligibleProgramDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ClaudeExplanationClient {

    private static final int MAX_TOKENS = 400;

    private final AnthropicClient anthropicClient;

    public ClaudeExplanationClient(AnthropicClient anthropicClient) {
        this.anthropicClient = anthropicClient;
    }

    public String explainRecommendation(String customerSummary, List<EligibleProgramDTO> rankedPrograms,
                                        BigDecimal transactionAmount) {
        if (rankedPrograms.isEmpty()) {
            return null;
        }
        return anthropicClient.complete(buildPrompt(customerSummary, rankedPrograms, transactionAmount), MAX_TOKENS);
    }

    private String buildPrompt(String customerSummary, List<EligibleProgramDTO> rankedPrograms, BigDecimal amount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tu es un assistant qui explique à un agent bancaire pourquoi un programme BNPL ")
                .append("a été recommandé à un client. Les scores et critères ci-dessous ont déjà été ")
                .append("calculés par un moteur de règles — tu ne dois PAS recalculer ni inventer de chiffres, ")
                .append("seulement les reformuler clairement en 3-4 phrases en français, en argumentant pourquoi ")
                .append("le programme en tête de liste est le meilleur choix, et en mentionnant brièvement ")
                .append("une alternative si pertinente.\n\n");

        sb.append("Profil client : ").append(customerSummary).append("\n");
        if (amount != null) {
            sb.append("Montant de la transaction envisagée : ").append(amount).append("\n");
        }
        sb.append("\nProgrammes éligibles classés par score (100 = meilleur) :\n");

        for (EligibleProgramDTO p : rankedPrograms) {
            sb.append("- ").append(p.getProgramName())
                    .append(" (score=").append(p.getScore())
                    .append(", frais=").append(p.getFeeSummary())
                    .append(", limite/transaction=").append(p.getMaxAmountPerTransaction())
                    .append(", critères validés=").append(p.getMatchedCriteria())
                    .append(")\n");
        }

        return sb.toString();
    }
}