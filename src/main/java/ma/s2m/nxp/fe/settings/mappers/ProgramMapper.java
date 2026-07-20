package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.Enums.Channel;
import ma.s2m.nxp.fe.settings.domain.program.*;
import ma.s2m.nxp.fe.settings.DTO.program.EligibilityDTO;
import ma.s2m.nxp.fe.settings.DTO.program.FeeDTO;
import ma.s2m.nxp.fe.settings.DTO.program.LimitDTO;
import ma.s2m.nxp.fe.settings.DTO.program.ProgramDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper Program (Entity) <-> ProgramDTO.
 * Note : institution est géré manuellement dans le service/orchestration
 * (résolution par institutionId), pas ici, car le mapper ne connait pas
 * le repository.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgramMapper {

    @Mapping(target = "institutionId", source = "institution.id")
    @Mapping(target = "institutionName", source = "institution.name")
    @Mapping(target = "type", expression = "java(program.getType() != null ? program.getType().name() : null)")
    @Mapping(target = "status", expression = "java(program.getStatus() != null ? program.getStatus().name() : null)")
    @Mapping(target = "eligibility", expression = "java(toEligibilityDTO(program))")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "limit", expression = "java(toLimitDTO(program))")
    ProgramDTO toDTO(Program program);

    default EligibilityDTO toEligibilityDTO(Program program) {
        if (program.getEligibility() == null) return null;
        return EligibilityDTO.builder()
                .minAge(program.getEligibility().getMinAge())
                .maxAge(program.getEligibility().getMaxAge())
                .minSalary(program.getEligibility().getMinSalary())
                .allowedSubBins(program.getAllowedSubBins())
                .build();
    }

    default LimitDTO toLimitDTO(Program program) {
        if (program.getLimit() == null) return null;
        java.util.Set<String> channels = program.getAllowedChannels() == null ? new java.util.HashSet<>()
                : program.getAllowedChannels().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet());
        return LimitDTO.builder()
                .maxAmountPerTransaction(program.getLimit().getMaxAmountPerTransaction())
                .maxTotalAmount(program.getLimit().getMaxTotalAmount())
                .maxMonthlyInstallment(program.getLimit().getMaxMonthlyInstallment())
                .mccCode(program.getLimit().getMccCode())
                .allowedChannels(channels)
                .build();
    }

    @Mapping(target = "feeType", expression = "java(fee.getFeeType() != null ? fee.getFeeType().name() : null)")
    FeeDTO toDTO(Fee fee);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institution", ignore = true) // résolu manuellement via institutionId
    @Mapping(target = "type", expression = "java(dto.getType() != null ? ma.s2m.nxp.fe.settings.Enums.ProgramType.valueOf(dto.getType()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.ProgramStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "eligibility", source = "eligibility")
    @Mapping(target = "allowedSubBins", expression = "java(dto.getEligibility() != null ? dto.getEligibility().getAllowedSubBins() : new java.util.HashSet<>())")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "limit", source = "limit")
    @Mapping(target = "allowedChannels", expression = "java(toChannelSet(dto))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Program toEntity(ProgramDTO dto);

    default java.util.Set<Channel> toChannelSet(ProgramDTO dto) {
        if (dto.getLimit() == null || dto.getLimit().getAllowedChannels() == null) return new java.util.HashSet<>();
        return dto.getLimit().getAllowedChannels().stream()
                .map(Channel::valueOf)
                .collect(java.util.stream.Collectors.toSet());
    }

    Eligibility toEntity(EligibilityDTO dto);

    @Mapping(target = "feeType", expression = "java(dto.getFeeType() != null ? ma.s2m.nxp.fe.settings.Enums.FeeType.valueOf(dto.getFeeType()) : null)")
    Fee toEntity(FeeDTO dto);

    Limit toEntity(LimitDTO dto);
}