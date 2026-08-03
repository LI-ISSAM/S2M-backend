package ma.s2m.nxp.fe.settings.orchestration.impl;

import ma.s2m.nxp.fe.settings.domain.installment_plan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.dto.installment_plan.InstallmentPlanDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.mappers.InstallmentPlanMapper;
import ma.s2m.nxp.fe.settings.orchestration.IInstallmentPlanOrchestrationService;
import ma.s2m.nxp.fe.settings.services.IInstallmentPlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallmentPlanOrchestrationServiceImpl implements IInstallmentPlanOrchestrationService {

    private final IInstallmentPlanService installmentPlanService;
    private final InstallmentPlanMapper installmentPlanMapper;

    public InstallmentPlanOrchestrationServiceImpl(IInstallmentPlanService installmentPlanService,
                                                   InstallmentPlanMapper installmentPlanMapper) {
        this.installmentPlanService = installmentPlanService;
        this.installmentPlanMapper = installmentPlanMapper;
    }

    @Override
    public InstallmentPlansPageResponse getAllInstallmentPlans(int page, int limit, String customerName,
                                                               String offerName, Long customerId, Long offerId) {
        int zeroBasedPage = Math.max(page - 1, 0);
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, limit, Sort.by(Sort.Direction.DESC, "startDate"));

        Page<InstallmentPlan> result = installmentPlanService.getAllInstallmentPlans(
                pageRequest, customerName, offerName, customerId, offerId);

        List<InstallmentPlanDTO> content = result.getContent().stream()
                .map(installmentPlanMapper::toDTO)
                .toList();

        return new InstallmentPlansPageResponse(content, result.getTotalElements(), result.getTotalPages(), page);
    }

    public List<InstallmentPlanDTO> getAllInstallmentPlansForExport(String customerName,
                                                                    String offerName, Long customerId, Long offerId) {
        Page<InstallmentPlan> result = installmentPlanService.getAllInstallmentPlans(Pageable.unpaged(),customerName,offerName,customerId,offerId);
        return result.getContent().stream()
                .map(installmentPlanMapper::toDTO)
                .toList();

    }

    @Override
    public InstallmentPlanDTO getInstallmentPlanById(Long id) throws BusinessException {
        InstallmentPlan plan = installmentPlanService.getInstallmentPlanById(id)
                .orElseThrow(() -> new BusinessException("IPL_002", "Installment Plan not found", HttpStatus.NOT_FOUND));
        return installmentPlanMapper.toDTO(plan);
    }

    @Override
    public InstallmentPlanDTO createInstallmentPlan(InstallmentPlanDTO dto) throws BusinessException {
        InstallmentPlan plan = installmentPlanMapper.toEntity(dto);
        InstallmentPlan saved = installmentPlanService.createInstallmentPlan(plan, dto.getCustomerId(), dto.getOfferId());
        return installmentPlanMapper.toDTO(saved);
    }

    @Override
    public InstallmentPlanDTO updateInstallmentPlan(Long id, InstallmentPlanDTO dto) throws BusinessException {
        InstallmentPlan newData = installmentPlanMapper.toEntity(dto);
        InstallmentPlan updated = installmentPlanService.updateInstallmentPlan(
                id, newData, dto.getCustomerId(), dto.getOfferId());
        return installmentPlanMapper.toDTO(updated);
    }

    @Override
    public void deleteInstallmentPlan(Long id) throws BusinessException {
        installmentPlanService.deleteInstallmentPlan(id);
    }
}