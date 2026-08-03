package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.installment.CustomerInstallment;
import ma.s2m.nxp.fe.settings.dto.installment.CustomerInstallmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerInstallmentMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerEmail", source = "customer.email")
    @Mapping(target = "status", expression = "java(installment.getStatus() != null ? installment.getStatus().name() : null)")
    CustomerInstallmentDTO toDTO(CustomerInstallment installment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true) // résolu manuellement via customerId
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? ma.s2m.nxp.fe.settings.enums.CustomerInstallmentStatus.valueOf(dto.getStatus()) : null)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CustomerInstallment toEntity(CustomerInstallmentDTO dto);
}