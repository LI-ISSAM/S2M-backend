package ma.s2m.nxp.fe.settings.services.ai;

import ma.s2m.nxp.fe.settings.dto.risk.CreditRiskScoreDTO;
import org.springframework.stereotype.Component;

@Component
public class GroqRiskExplanationClient {

    private static final int MAX_TOKENS = 350;

    private final GroqClient anthropicClient;

    public GroqRiskExplanationClient(GroqClient anthropicClient) {
        this.anthropicClient = anthropicClient;
    }

    public String explainRiskScore(CreditRiskScoreDTO riskScore) {
        return anthropicClient.complete(buildPrompt(riskScore), MAX_TOKENS);
    }

    private String buildPrompt(CreditRiskScoreDTO riskScore) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tu es un assistant qui explique à un agent de crédit un score de risque BNPL ")
                .append("qui a DÉJÀ été calculé par un moteur de règles. Tu ne dois PAS recalculer ni ")
                .append("inventer de chiffres, seulement reformuler clairement en 3-4 phrases en français : ")
                .append("le niveau de risque global, les facteurs qui l'expliquent le plus, et une ")
                .append("recommandation prudente (ex: surveiller, accorder avec limite réduite, refuser) ")
                .append("sans jamais formuler ça comme une décision automatique définitive — reste un outil ")
                .append("d'aide à la décision pour l'agent humain.\n\n");

        sb.append("Score de crédit : ").append(riskScore.getCreditScore()).append("/100\n");
        sb.append("Bande de risque : ").append(riskScore.getRiskBand()).append("\n");
        sb.append("Risque de retard sur la prochaine échéance : ")
                .append(riskScore.getNextInstallmentLatePaymentRisk()).append("%\n");
        sb.append("Historique de paiement : ").append(riskScore.getPaymentHistorySummary()).append("\n");
        sb.append("Facteurs pris en compte :\n");
        for (String factor : riskScore.getFactors()) {
            sb.append("- ").append(factor).append("\n");
        }

        return sb.toString();
    }
}