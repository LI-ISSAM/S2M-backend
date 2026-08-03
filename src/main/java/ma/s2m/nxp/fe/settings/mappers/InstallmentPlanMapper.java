package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.installment_plan.Installment;
import ma.s2m.nxp.fe.settings.domain.installment_plan.InstallmentPlan;
import ma.s2m.nxp.fe.settings.dto.installment_plan.InstallmentDTO;
import ma.s2m.nxp.fe.settings.dto.installment_plan.InstallmentPlanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper InstallmentPlan (Entity) <-> InstallmentPlanDTO.
 * Note : customer/offer sont résolus manuellement dans le service (via
 * customerId/offerId), et installments sont générés côté service
 * (InstallmentPlanService#generateSchedule) — jamais mappés depuis le DTO
 * entrant, uniquement en sortie (lecture seule).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstallmentPlanMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "offerId", source = "offer.id")
    @Mapping(target = "offerName", source = "offer.name")
    @Mapping(target = "status", expression = "java(plan.getStatus() != null ? plan.getStatus().name() : null)")
    @Mapping(target = "installments", source = "installments")
    InstallmentPlanDTO toDTO(InstallmentPlan plan);

    @Mapping(target = "status", expression = "java(installment.getStatus() != null ? installment.getStatus().name() : null)")
    InstallmentDTO toDTO(Installment installment);

    List<InstallmentDTO> toDTOList(List<Installment> installments);

    // ---------- DTO -> Entity (champs simples uniquement) ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true) // résolu manuellement via customerId
    @Mapping(target = "offer", ignore = true)     // résolu manuellement via offerId
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.enums.InstallmentPlanStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "installments", ignore = true) // généré côté service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InstallmentPlan toEntity(InstallmentPlanDTO dto);
}