package ma.s2m.nxp.fe.settings.orchestration;

import ma.s2m.nxp.fe.settings.DTO.installmentplan.InstallmentPlanDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.impl.InstallmentPlansPageResponse;

public interface IInstallmentPlanOrchestrationService {

    InstallmentPlansPageResponse getAllInstallmentPlans(int page, int limit, String customerName, String offerName,
                                                        Long customerId, Long offerId);

    InstallmentPlanDTO getInstallmentPlanById(Long id) throws BusinessException;

    InstallmentPlanDTO createInstallmentPlan(InstallmentPlanDTO dto) throws BusinessException;

    InstallmentPlanDTO updateInstallmentPlan(Long id, InstallmentPlanDTO dto) throws BusinessException;

    void deleteInstallmentPlan(Long id) throws BusinessException;
}