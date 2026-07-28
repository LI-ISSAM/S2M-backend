package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.forceclosureinquiry.ForceClosureInquiry;
import ma.s2m.nxp.fe.settings.dto.forceclosureinquiry.ForceClosureInquiryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ForceClosureInquiryMapper {

    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardNumber", source = "card.cardNumber")
    ForceClosureInquiryDTO toDTO(ForceClosureInquiry forceClosureInquiry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true) // résolu manuellement via cardId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ForceClosureInquiry toEntity(ForceClosureInquiryDTO dto);
}