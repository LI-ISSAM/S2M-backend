package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.DTO.rescheduleinquiry.RescheduleInquiryDTO;
import ma.s2m.nxp.fe.settings.domain.rescheduleinquiry.RescheduleInquiry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RescheduleInquiryMapper {

    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardNumber", source = "card.cardNumber")
    RescheduleInquiryDTO toDTO(RescheduleInquiry rescheduleInquiry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true) // résolu manuellement via cardId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RescheduleInquiry toEntity(RescheduleInquiryDTO dto);
}