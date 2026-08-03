package ma.s2m.nxp.fe.settings.mappers;

import ma.s2m.nxp.fe.settings.domain.customer.*;
import ma.s2m.nxp.fe.settings.dto.customer.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "creationDate", source = "customerCreationDate")
    CustomerDTO toDTO(Customer customer);

    CustomerAddressDTO toDTO(CustomerAddress address);
    CustomerAccountDTO toDTO(CustomerAccount account);
    CustomerCardInfoDTO toDTO(CustomerCardInfo card);
    CustomerRoutingDTO toDTO(CustomerRouting routing);
    CustomerLinkDTO toDTO(CustomerLink link);

    // ---------- DTO -> Entity ----------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true) // généré par le backend
    @Mapping(target = "fullName", ignore = true)    // recalculé automatiquement (@PrePersist/@PreUpdate)
    @Mapping(target = "customerCreationDate", source = "creationDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerDTO dto);

    CustomerAddress toEntity(CustomerAddressDTO dto);
    CustomerAccount toEntity(CustomerAccountDTO dto);
    CustomerCardInfo toEntity(CustomerCardInfoDTO dto);
    CustomerRouting toEntity(CustomerRoutingDTO dto);
    CustomerLink toEntity(CustomerLinkDTO dto);
}