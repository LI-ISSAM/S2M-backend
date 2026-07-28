package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.DTO.programrecommendation.ProgramRecommendationResponseDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;

import java.math.BigDecimal;

public interface IProgramRecommendationService {

    ProgramRecommendationResponseDTO recommend(Long customerId, BigDecimal transactionAmount,
                                               boolean includeAiExplanation) throws BusinessException;
}