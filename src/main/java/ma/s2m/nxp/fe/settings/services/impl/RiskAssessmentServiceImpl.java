package ma.s2m.nxp.fe.settings.services.impl;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.domain.installmentplan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.DTO.risk.CreditRiskScoreDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.repositories.CustomerInstallmentRepository;
import ma.s2m.nxp.fe.settings.repositories.CustomerRepository;
import ma.s2m.nxp.fe.settings.repositories.InstallmentPlanRepository;
import ma.s2m.nxp.fe.settings.services.IRiskAssessmentService;
import ma.s2m.nxp.fe.settings.services.ai.ClaudeRiskExplanationClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RiskAssessmentServiceImpl implements IRiskAssessmentService {

    private final CustomerRepository customerRepository;
    private final CustomerInstallmentRepository customerInstallmentRepository;
    private final InstallmentPlanRepository installmentPlanRepository;
    private final RiskScoringService riskScoringService;
    private final ClaudeRiskExplanationClient claudeRiskExplanationClient;

    public RiskAssessmentServiceImpl(CustomerRepository customerRepository,
                                     CustomerInstallmentRepository customerInstallmentRepository,
                                     InstallmentPlanRepository installmentPlanRepository,
                                     RiskScoringService riskScoringService,
                                     ClaudeRiskExplanationClient claudeRiskExplanationClient) {
        this.customerRepository = customerRepository;
        this.customerInstallmentRepository = customerInstallmentRepository;
        this.installmentPlanRepository = installmentPlanRepository;
        this.riskScoringService = riskScoringService;
        this.claudeRiskExplanationClient = claudeRiskExplanationClient;
    }

    @Override
    public CreditRiskScoreDTO assessRisk(Long customerId, boolean includeAiExplanation) throws BusinessException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("RSK_001", "Customer not found", HttpStatus.NOT_FOUND));

        List<CustomerInstallment> history = customerInstallmentRepository.findAllByCustomer_Id(customerId);
        List<InstallmentPlan> plans = installmentPlanRepository.findAllByCustomer_Id(customerId);

        CreditRiskScoreDTO riskScore = riskScoringService.computeCreditRiskScore(customer, history, plans);

        if (includeAiExplanation) {
            String explanation = claudeRiskExplanationClient.explainRiskScore(riskScore);
            riskScore.setAiExplanation(explanation);
        }

        return riskScore;
    }
}