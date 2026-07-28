package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.operation.Operation;
import ma.s2m.nxp.fe.settings.DTO.operation.OperationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationMapper {

    @Mapping(target = "merchantId", source = "merchant.id")
    @Mapping(target = "merchantReference", source = "merchant.reference")
    @Mapping(target = "bnplProgramId", source = "bnplProgram.id")
    @Mapping(target = "bnplProgramName", source = "bnplProgram.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerEmail", source = "customer.email")
    OperationDTO toDTO(Operation operation);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchant", ignore = true)   // résolu manuellement via merchantId
    @Mapping(target = "bnplProgram", ignore = true) // résolu manuellement via bnplProgramId
    @Mapping(target = "customer", ignore = true)    // résolu manuellement via customerEmail
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Operation toEntity(OperationDTO dto);
}