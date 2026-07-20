package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.DTO.merchant.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper {

    @Mapping(target = "institutionId", source = "institution.id")
    @Mapping(target = "institutionName", source = "institution.name")
    @Mapping(target = "type", expression = "java(merchant.getType() != null ? merchant.getType().name() : null)")
    @Mapping(target = "status", expression = "java(merchant.getStatus() != null ? merchant.getStatus().name() : null)")
    MerchantDTO toDTO(Merchant merchant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institution", ignore = true) // résolu manuellement via institutionId
    @Mapping(target = "type", expression = "java(dto.getType() != null ? ma.s2m.nxp.fe.settings.Enums.MerchantType.valueOf(dto.getType()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.MerchantStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Merchant toEntity(MerchantDTO dto);
}