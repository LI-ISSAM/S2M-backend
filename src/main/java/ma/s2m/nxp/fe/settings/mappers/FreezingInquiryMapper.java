package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.freezinginquiry.FreezingInquiry;
import ma.s2m.nxp.fe.settings.DTO.freezinginquiry.FreezingInquiryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreezingInquiryMapper {

    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardNumber", source = "card.cardNumber")
    FreezingInquiryDTO toDTO(FreezingInquiry freezingInquiry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true) // résolu manuellement via cardId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FreezingInquiry toEntity(FreezingInquiryDTO dto);
}