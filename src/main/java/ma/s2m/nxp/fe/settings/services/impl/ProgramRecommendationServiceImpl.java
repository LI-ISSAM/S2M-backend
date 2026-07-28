package ma.s2m.nxp.fe.settings.services.impl;

import lombok.extern.slf4j.Slf4j;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.program.Program;
import ma.s2m.nxp.fe.settings.DTO.programrecommendation.EligibleProgramDTO;
import ma.s2m.nxp.fe.settings.DTO.programrecommendation.ProgramRecommendationResponseDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.ProgramRepository;
import ma.s2m.nxp.fe.settings.services.IProgramRecommendationService;
import ma.s2m.nxp.fe.settings.services.ai.ClaudeExplanationClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProgramRecommendationServiceImpl implements IProgramRecommendationService {

    private static final int MAX_CANDIDATES_FOR_AI = 5;

    private final CustomerRepository customerRepository;
    private final ProgramRepository programRepository;
    private final ProgramScoringService programScoringService;
    private final ClaudeExplanationClient claudeExplanationClient;

    public ProgramRecommendationServiceImpl(CustomerRepository customerRepository, ProgramRepository programRepository,
                                            ProgramScoringService programScoringService,
                                            ClaudeExplanationClient claudeExplanationClient) {
        this.customerRepository = customerRepository;
        this.programRepository = programRepository;
        this.programScoringService = programScoringService;
        this.claudeExplanationClient = claudeExplanationClient;
    }

    @Override
    public ProgramRecommendationResponseDTO recommend(Long customerId, BigDecimal transactionAmount,
                                                      boolean includeAiExplanation) throws BusinessException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("REC_001", "Customer not found", HttpStatus.NOT_FOUND));

        List<Program> allPrograms = programRepository.findAll();
        List<EligibleProgramDTO> ranked = programScoringService.scoreEligiblePrograms(customer, allPrograms, transactionAmount);

        Long recommendedId = ranked.isEmpty() ? null : ranked.get(0).getProgramId();

        String aiExplanation = null;
        if (includeAiExplanation && !ranked.isEmpty()) {
            List<EligibleProgramDTO> topCandidates = ranked.size() > MAX_CANDIDATES_FOR_AI
                    ? ranked.subList(0, MAX_CANDIDATES_FOR_AI) : ranked;
            String customerSummary = buildCustomerSummary(customer);
            aiExplanation = claudeExplanationClient.explainRecommendation(customerSummary, topCandidates, transactionAmount);
        }

        if (ranked.isEmpty()) {
            log.info("Aucun programme éligible trouvé pour customerId={}", customerId);
        }

        return ProgramRecommendationResponseDTO.builder()
                .customerId(customerId)
                .transactionAmount(transactionAmount)
                .eligiblePrograms(ranked)
                .recommendedProgramId(recommendedId)
                .aiExplanation(aiExplanation)
                .build();
    }

    private String buildCustomerSummary(Customer customer) {
        return String.format("nom=%s, segment=%s, subBin=%s, revenu brut=%s, niveau de risque=%s",
                customer.getFullName(),
                customer.getCustomerSegment(),
                customer.getSubBin(),
                customer.getGrossIncome(),
                customer.getRiskLevel());
    }
}