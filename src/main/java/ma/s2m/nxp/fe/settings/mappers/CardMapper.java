package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.card.Card;
import ma.s2m.nxp.fe.settings.DTO.card.CardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.ZoneOffset;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerEmail", source = "customer.contact.email")
    @Mapping(target = "programId", source = "program.id")
    @Mapping(target = "programName", source = "program.name")
    @Mapping(target = "type", expression = "java(card.getType() != null ? card.getType().name() : null)")
    @Mapping(target = "status", expression = "java(card.getStatus() != null ? card.getStatus().name() : null)")
    @Mapping(target = "creationDate", expression = "java(card.getCreatedAt() != null ? card.getCreatedAt().atZone(java.time.ZoneOffset.UTC).toLocalDate() : null)")
    CardDTO toDTO(Card card);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true) // résolu manuellement via customerId
    @Mapping(target = "program", ignore = true)   // résolu manuellement via programId
    @Mapping(target = "type", expression = "java(dto.getType() != null ? ma.s2m.nxp.fe.settings.Enums.CardType.valueOf(dto.getType()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.CardStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Card toEntity(CardDTO dto);
}