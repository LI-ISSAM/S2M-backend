package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.domain.installment_plan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IInstallmentPlanService {

    Page<InstallmentPlan> getAllInstallmentPlans(Pageable pageable, String customerName, String offerName,
                                                 Long customerId, Long offerId);

    Optional<InstallmentPlan> getInstallmentPlanById(Long id);

    InstallmentPlan createInstallmentPlan(InstallmentPlan plan, Long customerId, Long offerId) throws BusinessException;

    InstallmentPlan updateInstallmentPlan(Long id, InstallmentPlan newData, Long customerId, Long offerId)
            throws BusinessException;

    void deleteInstallmentPlan(Long id) throws BusinessException;
}