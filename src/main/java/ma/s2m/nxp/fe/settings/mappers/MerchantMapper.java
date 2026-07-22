package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.merchant.*;
import ma.s2m.nxp.fe.settings.DTO.merchant.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper {

    @Mapping(target = "institutionId", source = "institution.id")
    @Mapping(target = "institutionName", source = "institution.name")
    @Mapping(target = "type", expression = "java(merchant.getType() != null ? merchant.getType().name() : null)")
    @Mapping(target = "status", expression = "java(merchant.getStatus() != null ? merchant.getStatus().name() : null)")
    @Mapping(target = "creationDate", source = "businessCreationDate")
    MerchantDTO toDTO(Merchant merchant);

    // Sous-objets : noms de champs identiques, MapStruct mappe automatiquement.
    MerchantOwnerDTO toDTO(MerchantOwner owner);
    MerchantCurrencySupportedDTO toDTO(MerchantCurrencySupported currency);
    MerchantAccountDTO toDTO(MerchantAccount account);
    MerchantAccountRoutingDTO toDTO(MerchantAccountRouting routing);
    MerchantMembershipFeeDTO toDTO(MerchantMembershipFee fee);
    MerchantCommissionDTO toDTO(MerchantCommission commission);
    MerchantAddressDTO toDTO(MerchantAddress address);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institution", ignore = true) // résolu manuellement via institutionId
    @Mapping(target = "type", expression = "java(dto.getType() != null ? ma.s2m.nxp.fe.settings.Enums.MerchantType.valueOf(dto.getType()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.MerchantStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "businessCreationDate", source = "creationDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Merchant toEntity(MerchantDTO dto);

    MerchantOwner toEntity(MerchantOwnerDTO dto);
    MerchantCurrencySupported toEntity(MerchantCurrencySupportedDTO dto);
    MerchantAccount toEntity(MerchantAccountDTO dto);
    MerchantAccountRouting toEntity(MerchantAccountRoutingDTO dto);
    MerchantMembershipFee toEntity(MerchantMembershipFeeDTO dto);
    MerchantCommission toEntity(MerchantCommissionDTO dto);
    MerchantAddress toEntity(MerchantAddressDTO dto);
}