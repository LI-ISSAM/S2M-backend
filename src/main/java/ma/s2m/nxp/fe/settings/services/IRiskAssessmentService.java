package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.DTO.risk.CreditRiskScoreDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;

public interface IRiskAssessmentService {

    CreditRiskScoreDTO assessRisk(Long customerId, boolean includeAiExplanation) throws BusinessException;
}