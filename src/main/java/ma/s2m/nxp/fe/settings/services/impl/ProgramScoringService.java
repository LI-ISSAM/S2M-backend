package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.enums.ProgramStatus;
import ma.s2m.nxp.fe.settings.dto.program_recommendation.EligibleProgramDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ProgramScoringService {

    private static final double WEIGHT_FEE = 0.5;
    private static final double WEIGHT_LIMIT = 0.3;
    private static final double WEIGHT_SEGMENT = 0.2;

    public List<EligibleProgramDTO> scoreEligiblePrograms(Customer customer, List<Program> allPrograms,
                                                          BigDecimal transactionAmount) {
        List<CandidateContext> eligible = new ArrayList<>();

        for (Program program : allPrograms) {
            Optional<List<String>> matchedCriteria = checkEligibility(customer, program, transactionAmount);
            matchedCriteria.ifPresent(criteria -> eligible.add(new CandidateContext(program, criteria)));
        }

        if (eligible.isEmpty()) {
            log.debug("Aucun programme éligible pour customerId={}", customer.getId());
            return List.of();
        }

        BigDecimal minFee = eligible.stream().map(c -> feeProxy(c.program))
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxFee = eligible.stream().map(c -> feeProxy(c.program))
                .max(BigDecimal::compareTo).orElse(BigDecimal.ONE);
        BigDecimal minLimit = eligible.stream().map(c -> limitProxy(c.program))
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxLimit = eligible.stream().map(c -> limitProxy(c.program))
                .max(BigDecimal::compareTo).orElse(BigDecimal.ONE);

        List<EligibleProgramDTO> result = new ArrayList<>();
        for (CandidateContext ctx : eligible) {
            double feeScore = normalizeInverse(feeProxy(ctx.program), minFee, maxFee);
            double limitScore = normalize(limitProxy(ctx.program), minLimit, maxLimit);
            double segmentScore = segmentMatchScore(customer, ctx.program);

            double finalScore = (WEIGHT_FEE * feeScore) + (WEIGHT_LIMIT * limitScore) + (WEIGHT_SEGMENT * segmentScore);

            result.add(EligibleProgramDTO.builder()
                    .programId(ctx.program.getId())
                    .programName(ctx.program.getName())
                    .programType(ctx.program.getType() != null ? ctx.program.getType().name() : null)
                    .score(Math.round(finalScore * 100.0) / 100.0)
                    .feeSummary(formatFee(ctx.program))
                    .maxAmountPerTransaction(ctx.program.getLimit() != null ? ctx.program.getLimit().getMaxAmountPerTransaction() : null)
                    .maxMonthlyInstallment(ctx.program.getLimit() != null ? ctx.program.getLimit().getMaxMonthlyInstallment() : null)
                    .matchedCriteria(ctx.matchedCriteria)
                    .build());
        }

        result.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return result;
    }

    private Optional<List<String>> checkEligibility(Customer customer, Program program, BigDecimal transactionAmount) {
        if (program.getStatus() != ProgramStatus.ACTIVE) {
            log.debug("Programme {} rejeté : statut {}", program.getName(), program.getStatus());
            return Optional.empty();
        }
        if (program.getEligibility() == null) {
            log.debug("Programme {} rejeté : eligibility non définie", program.getName());
            return Optional.empty();
        }

        List<String> matched = new ArrayList<>();

        int age = customer.getBirthDate() != null
                ? Period.between(customer.getBirthDate(), LocalDate.now()).getYears()
                : -1;
        Integer minAge = program.getEligibility().getMinAge();
        Integer maxAge = program.getEligibility().getMaxAge();
        if (minAge != null && maxAge != null) {
            if (age < minAge || age > maxAge) {
                log.debug("Programme {} rejeté : âge {} hors [{}, {}]", program.getName(), age, minAge, maxAge);
                return Optional.empty();
            }
            matched.add("Âge éligible (" + age + " ans, entre " + minAge + " et " + maxAge + ")");
        }

        BigDecimal minSalary = program.getEligibility().getMinSalary();
        BigDecimal grossIncome = customer.getGrossIncome();
        if (minSalary != null) {
            if (grossIncome == null || grossIncome.compareTo(minSalary) < 0) {
                log.debug("Programme {} rejeté : revenu {} < minimum {}", program.getName(), grossIncome, minSalary);
                return Optional.empty();
            }
            matched.add("Revenu suffisant (" + grossIncome + " ≥ " + minSalary + ")");
        }

        if (program.getAllowedSubBins() != null && !program.getAllowedSubBins().isEmpty()) {
            String customerSubBin = customer.getSubBin() != null ? customer.getSubBin().name() : null;
            if (customerSubBin == null || !program.getAllowedSubBins().contains(customerSubBin)) {
                log.debug("Programme {} rejeté : subBin {} non autorisé (autorisés={})",
                        program.getName(), customerSubBin, program.getAllowedSubBins());
                return Optional.empty();
            }
            matched.add("Sous-réseau carte autorisé (" + customerSubBin + ")");
        }

        if (transactionAmount != null && program.getLimit() != null
                && program.getLimit().getMaxAmountPerTransaction() != null) {
            if (transactionAmount.compareTo(program.getLimit().getMaxAmountPerTransaction()) > 0) {
                log.debug("Programme {} rejeté : montant {} > limite {}",
                        program.getName(), transactionAmount, program.getLimit().getMaxAmountPerTransaction());
                return Optional.empty();
            }
            matched.add("Montant de transaction couvert (≤ " + program.getLimit().getMaxAmountPerTransaction() + ")");
        }

        return Optional.of(matched);
    }

    private double segmentMatchScore(Customer customer, Program program) {
        if (customer.getCustomerSegment() == null || program.getType() == null) {
            return 50.0;
        }
        String segment = customer.getCustomerSegment().toLowerCase();
        String type = program.getType().name().toLowerCase();
        boolean premiumMatch = (segment.contains("premium") || segment.contains("vip")) && type.contains("premium");
        boolean standardMatch = segment.contains("retail") && type.contains("standard");
        return (premiumMatch || standardMatch) ? 100.0 : 50.0;
    }

    private BigDecimal feeProxy(Program program) {
        if (program.getFee() == null || program.getFee().getAmount() == null) return BigDecimal.ZERO;
        return program.getFee().getAmount();
    }

    private BigDecimal limitProxy(Program program) {
        if (program.getLimit() == null || program.getLimit().getMaxAmountPerTransaction() == null) {
            return new BigDecimal("999999999");
        }
        return program.getLimit().getMaxAmountPerTransaction();
    }

    private String formatFee(Program program) {
        if (program.getFee() == null) return "-";
        String type = program.getFee().getFeeType() != null ? program.getFee().getFeeType().name() : "-";
        BigDecimal amount = program.getFee().getAmount() != null ? program.getFee().getAmount() : BigDecimal.ZERO;
        return type + " " + amount;
    }

    private double normalize(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (max.compareTo(min) == 0) return 100.0;
        return value.subtract(min).multiply(BigDecimal.valueOf(100))
                .divide(max.subtract(min), 2, java.math.RoundingMode.HALF_UP).doubleValue();
    }

    private double normalizeInverse(BigDecimal value, BigDecimal min, BigDecimal max) {
        return 100.0 - normalize(value, min, max);
    }

    private record CandidateContext(Program program, List<String> matchedCriteria) {
    }
}