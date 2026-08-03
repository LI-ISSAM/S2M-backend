package ma.s2m.nxp.fe.settings.services.impl;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.enums.CustomerInstallmentStatus;
import ma.s2m.nxp.fe.settings.domain.installment_plan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.enums.InstallmentPlanStatus;
import ma.s2m.nxp.fe.settings.dto.risk.CreditRiskScoreDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class RiskScoringService {

    private static final double WEIGHT_PAYMENT_HISTORY = 0.40;
    private static final double WEIGHT_DEBT_TO_INCOME = 0.30;
    private static final double WEIGHT_DECLARED_RISK = 0.20;
    private static final double WEIGHT_DEPENDENTS = 0.10;

    public CreditRiskScoreDTO computeCreditRiskScore(Customer customer, List<CustomerInstallment> history,
                                                     List<InstallmentPlan> plans) {
        List<String> factors = new ArrayList<>();

        double paymentHistoryScore = scorePaymentHistory(history, factors);
        double debtToIncomeScore = scoreDebtToIncome(customer, plans, factors);
        double declaredRiskScore = scoreDeclaredRisk(customer, factors);
        double dependentsScore = scoreDependents(customer, factors);

        double weighted = (WEIGHT_PAYMENT_HISTORY * paymentHistoryScore)
                + (WEIGHT_DEBT_TO_INCOME * debtToIncomeScore)
                + (WEIGHT_DECLARED_RISK * declaredRiskScore)
                + (WEIGHT_DEPENDENTS * dependentsScore);

        int creditScore = (int) Math.round(Math.max(0, Math.min(100, weighted)));
        String riskBand = creditScore >= 75 ? "LOW" : creditScore >= 50 ? "MEDIUM" : "HIGH";

        int lateRisk = computeNextInstallmentLateRisk(creditScore, history);

        return CreditRiskScoreDTO.builder()
                .customerId(customer.getId())
                .creditScore(creditScore)
                .riskBand(riskBand)
                .nextInstallmentLatePaymentRisk(lateRisk)
                .paymentHistorySummary(buildPaymentHistorySummary(history))
                .factors(factors)
                .build();
    }

    private double scorePaymentHistory(List<CustomerInstallment> history, List<String> factors) {
        if (history == null || history.isEmpty()) {
            factors.add("Aucun historique de paiement disponible (score neutre appliqué)");
            return 60.0;
        }

        long total = history.size();
        long late = history.stream().filter(i -> i.getStatus() == CustomerInstallmentStatus.LATE).count();
        double lateRatio = (double) late / total;
        double score = 100.0 - (lateRatio * 100.0);

        factors.add(String.format("Historique de paiement : %d/%d échéance(s) en retard (%.0f%%)",
                late, total, lateRatio * 100));
        return Math.max(0, score);
    }

    private double scoreDebtToIncome(Customer customer, List<InstallmentPlan> plans, List<String> factors) {
        BigDecimal grossIncome = customer.getGrossIncome();
        if (grossIncome == null || grossIncome.compareTo(BigDecimal.ZERO) <= 0) {
            factors.add("Revenu brut non renseigné (score neutre appliqué)");
            return 50.0;
        }

        BigDecimal totalOutstanding = plans == null ? BigDecimal.ZERO : plans.stream()
                .filter(p -> p.getStatus() == InstallmentPlanStatus.ACTIVE || p.getStatus() == InstallmentPlanStatus.PENDING)
                .map(InstallmentPlan::getTotalAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double ratio = totalOutstanding.divide(grossIncome, 4, java.math.RoundingMode.HALF_UP).doubleValue();
        double score = 100.0 - ((ratio - 0.1) / 0.9) * 100.0;
        score = Math.max(0, Math.min(100, score));

        factors.add(String.format("Endettement en cours : %s pour un revenu brut de %s (ratio %.0f%%)",
                totalOutstanding, grossIncome, ratio * 100));
        return score;
    }

    private double scoreDeclaredRisk(Customer customer, List<String> factors) {
        String riskLevel = customer.getRiskLevel();
        if (riskLevel == null || riskLevel.isBlank()) {
            return 60.0;
        }
        String normalized = riskLevel.toLowerCase();
        double score;
        if (normalized.contains("low") || normalized.contains("faible")) {
            score = 100.0;
        } else if (normalized.contains("high") || normalized.contains("élevé") || normalized.contains("eleve")) {
            score = 20.0;
        } else if (normalized.contains("medium") || normalized.contains("moyen")) {
            score = 60.0;
        } else {
            score = 50.0;
        }
        factors.add("Niveau de risque déclaré : " + riskLevel);
        return score;
    }

    private double scoreDependents(Customer customer, List<String> factors) {
        Integer dependents = customer.getDependents();
        if (dependents == null) {
            return 70.0;
        }
        double score;
        if (dependents <= 1) score = 100.0;
        else if (dependents <= 3) score = 70.0;
        else score = 40.0;

        factors.add(dependents + " personne(s) à charge");
        return score;
    }

    private int computeNextInstallmentLateRisk(int creditScore, List<CustomerInstallment> history) {
        double risk = 100.0 - creditScore;

        boolean currentlyLate = history != null && history.stream()
                .anyMatch(i -> i.getStatus() == CustomerInstallmentStatus.LATE);
        if (currentlyLate) {
            risk = Math.min(100, risk + 20);
        }

        boolean perfectHistory = history != null && history.size() >= 3
                && history.stream().noneMatch(i -> i.getStatus() == CustomerInstallmentStatus.LATE);
        if (perfectHistory) {
            risk = Math.max(0, risk - 10);
        }

        return (int) Math.round(risk);
    }

    private String buildPaymentHistorySummary(List<CustomerInstallment> history) {
        if (history == null || history.isEmpty()) {
            return "Aucun historique de paiement";
        }
        long total = history.size();
        long paid = history.stream().filter(i -> i.getStatus() == CustomerInstallmentStatus.PAID).count();
        long late = history.stream().filter(i -> i.getStatus() == CustomerInstallmentStatus.LATE).count();
        return String.format("%d échéance(s) payée(s), %d en retard sur %d au total (%.0f%% de retard)",
                paid, late, total, total > 0 ? (late * 100.0 / total) : 0);
    }
}