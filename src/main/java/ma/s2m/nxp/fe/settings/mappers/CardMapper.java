package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.card.*;
import ma.s2m.nxp.fe.settings.DTO.card.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.ZoneOffset;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerEmail", source = "customer.email")
    @Mapping(target = "programId", source = "program.id")
    @Mapping(target = "programName", source = "program.name")
    @Mapping(target = "type", expression = "java(card.getType() != null ? card.getType().name() : null)")
    @Mapping(target = "status", expression = "java(card.getStatus() != null ? card.getStatus().name() : null)")
    @Mapping(target = "creationDate", expression = "java(card.getCreatedAt() != null ? card.getCreatedAt().atZone(java.time.ZoneOffset.UTC).toLocalDate() : null)")
    CardDTO toDTO(Card card);

    // Sous-objets : les noms de champs sont identiques entre l'entité et le DTO,
    // MapStruct génère donc le mapping automatiquement (pas de @Mapping requis).
    CustomerDataDTO toDTO(CustomerData customerData);
    CardInfoDTO toDTO(CardInfo cardInfo);
    AdditionalDataDTO toDTO(AdditionalData additionalData);
    CommissionDTO toDTO(Commission commission);
    CardFeesDTO toDTO(CardFees cardFees);
    ReplacementDataDTO toDTO(ReplacementData replacementData);
    RenewDataDTO toDTO(RenewData renewData);
    RecalculPinDTO toDTO(RecalculPin recalculPin);
    PersonalizationDataDTO toDTO(PersonalizationData personalizationData);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true) // résolu manuellement via customerId
    @Mapping(target = "program", ignore = true)   // résolu manuellement via programId
    @Mapping(target = "type", expression = "java(dto.getType() != null ? ma.s2m.nxp.fe.settings.Enums.CardType.valueOf(dto.getType()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.CardStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Card toEntity(CardDTO dto);

    CustomerData toEntity(CustomerDataDTO dto);
    CardInfo toEntity(CardInfoDTO dto);
    AdditionalData toEntity(AdditionalDataDTO dto);
    Commission toEntity(CommissionDTO dto);
    CardFees toEntity(CardFeesDTO dto);
    ReplacementData toEntity(ReplacementDataDTO dto);
    RenewData toEntity(RenewDataDTO dto);
    RecalculPin toEntity(RecalculPinDTO dto);
    PersonalizationData toEntity(PersonalizationDataDTO dto);
}