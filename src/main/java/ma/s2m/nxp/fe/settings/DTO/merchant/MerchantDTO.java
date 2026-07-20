package ma.s2m.nxp.fe.settings.DTO.merchant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantDTO {

    private Long id;

    @NotBlank(message = "Merchant Name is required")
    @Size(min = 2, max = 50, message = "Merchant Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Reference is required")
    @Size(min = 2, max = 30, message = "Reference must be between 2 and 30 characters")
    private String reference;

    @NotBlank(message = "MCC Code is required")
    private String mccCode;

    @NotNull(message = "Institution is required")
    private Long institutionId;

    /**
     * Champ en lecture seule, renvoyé par le backend pour affichage.
     */
    private String institutionName;

    @NotNull(message = "Type is required")
    private String type;   // RETAIL / ECOMMERCE / SERVICES

    @NotNull(message = "Status is required")
    private String status; // PENDING / ACTIVE / SUSPENDED / ARCHIVED
}