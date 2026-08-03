package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.member.Contact;
import ma.s2m.nxp.fe.settings.domain.member.Institution;

import ma.s2m.nxp.fe.settings.dto.institution.ContactDTO;
import ma.s2m.nxp.fe.settings.dto.institution.InstitutionDTO;
import ma.s2m.nxp.fe.settings.dto.institution.MetadataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


/**
 * Mapper Institution (Entity) <-> InstitutionDTO.
 * Le mapping des sous-objets contact/metadata est fait manuellement ci-dessous
 * car Institution "aplatit" description/onboardingDate/tags au niveau racine,
 * alors que le DTO les regroupe sous "metadata" (comme attendu par le frontend).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstitutionMapper {

    @Mapping(target = "type", expression = "java(institution.getType() != null ? institution.getType().name() : null)")
    @Mapping(target = "status", expression = "java(institution.getStatus() != null ? institution.getStatus().name() : null)")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "metadata", expression = "java(toMetadataDTO(institution))")
    InstitutionDTO toDTO(Institution institution);

    ContactDTO toDTO(Contact contact);

    @Mapping(target = "type", expression = "java(dto.getType() != null ? ma.s2m.nxp.fe.settings.enums.InstitutionType.valueOf(dto.getType()) : null)")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.enums.InstitutionStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "description", expression = "java(dto.getMetadata() != null ? dto.getMetadata().getDescription() : null)")
    @Mapping(target = "onboardingDate", expression = "java(dto.getMetadata() != null ? dto.getMetadata().getOnboardingDate() : null)")
    @Mapping(target = "tags", expression = "java(dto.getMetadata() != null ? dto.getMetadata().getTags() : new java.util.HashSet<>())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Institution toEntity(InstitutionDTO dto);

    Contact toEntity(ContactDTO dto);

    default MetadataDTO toMetadataDTO(Institution institution) {
        return MetadataDTO.builder()
                .description(institution.getDescription())
                .onboardingDate(institution.getOnboardingDate())
                .tags(institution.getTags())
                .build();
    }
}