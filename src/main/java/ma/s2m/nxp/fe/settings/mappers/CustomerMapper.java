package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.customer.CustomerContact;
import ma.s2m.nxp.fe.settings.DTO.customer.CustomerContactDTO;
import ma.s2m.nxp.fe.settings.DTO.customer.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "subBin", expression = "java(customer.getSubBin() != null ? customer.getSubBin().name() : null)")
    @Mapping(target = "contact", source = "contact")
    CustomerDTO toDTO(Customer customer);

    CustomerContactDTO toDTO(CustomerContact contact);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true) // généré par le backend
    @Mapping(target = "subBin", expression = "java(dto.getSubBin() != null ? ma.s2m.nxp.fe.settings.Enums.SubBin.valueOf(dto.getSubBin()) : null)")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerDTO dto);

    CustomerContact toEntity(CustomerContactDTO dto);
}