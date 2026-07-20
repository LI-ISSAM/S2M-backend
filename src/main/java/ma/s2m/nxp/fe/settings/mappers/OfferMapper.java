package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.domain.offer.OfferFee;
import ma.s2m.nxp.fe.settings.domain.offer.OfferLimit;
import ma.s2m.nxp.fe.settings.DTO.offer.OfferDTO;
import ma.s2m.nxp.fe.settings.DTO.offer.OfferFeeDTO;
import ma.s2m.nxp.fe.settings.DTO.offer.OfferLimitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper Offer (Entity) <-> OfferDTO.
 * Note : program est géré manuellement dans le service/orchestration
 * (résolution par programId), pas ici.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {

    @Mapping(target = "programId", source = "program.id")
    @Mapping(target = "programName", source = "program.name")
    @Mapping(target = "status", expression = "java(offer.getStatus() != null ? offer.getStatus().name() : null)")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "limit", source = "limit")
    OfferDTO toDTO(Offer offer);

    @Mapping(target = "feeType", expression = "java(fee.getFeeType() != null ? fee.getFeeType().name() : null)")
    OfferFeeDTO toDTO(OfferFee fee);

    @Mapping(target = "allowedChannel", expression = "java(limit.getAllowedChannel() != null ? limit.getAllowedChannel().name() : null)")
    OfferLimitDTO toDTO(OfferLimit limit);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "program", ignore = true) // résolu manuellement via programId
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.Enums.OfferStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "limit", source = "limit")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Offer toEntity(OfferDTO dto);

    @Mapping(target = "feeType", expression = "java(dto.getFeeType() != null ? ma.s2m.nxp.fe.settings.Enums.FeeType.valueOf(dto.getFeeType()) : null)")
    OfferFee toEntity(OfferFeeDTO dto);

    @Mapping(target = "allowedChannel", expression = "java(dto.getAllowedChannel() != null && !dto.getAllowedChannel().isBlank() ? ma.s2m.nxp.fe.settings.Enums.Channel.valueOf(dto.getAllowedChannel()) : null)")
    OfferLimit toEntity(OfferLimitDTO dto);
}