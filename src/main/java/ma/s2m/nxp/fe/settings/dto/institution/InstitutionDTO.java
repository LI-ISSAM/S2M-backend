package ma.s2m.nxp.fe.settings.dto.institution;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstitutionDTO {

    private Long id;

    @NotBlank(message = "Institution Name is required")
    @Size(min = 2, max = 50, message = "Institution Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Reference is required")
    @Size(min = 2, max = 20, message = "Reference must be between 2 and 20 characters")
    private String reference;

    @NotNull(message = "Type is required")
    private String type;   // BANK / ISSUER / FINTECH

    @NotNull(message = "Status is required")
    private String status; // PENDING / ACTIVE / SUSPENDED / ARCHIVED

    private String logo;

    @Valid
    @NotNull(message = "Contact is required")
    private ContactDTO contact;

    @Valid
    private MetadataDTO metadata;
}