package ma.s2m.nxp.fe.settings.dto.program;

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
public class ProgramDTO {

    private Long id;

    @NotBlank(message = "Program Name is required")
    @Size(min = 2, max = 50, message = "Program Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Institution is required")
    private Long institutionId;

    /**
     * Champ en lecture seule, renvoyé par le backend pour affichage
     * (ex: liste des programmes) - ignoré si envoyé à la création/update.
     */
    private String institutionName;

    @NotNull(message = "Type is required")
    private String type;   // STANDARD / PREMIUM / LEGENDE

    @NotNull(message = "Status is required")
    private String status; // PENDING / ACTIVE / SUSPENDED / ARCHIVED

    @Valid
    @NotNull(message = "Eligibility is required")
    private EligibilityDTO eligibility;

    @Valid
    @NotNull(message = "Fee is required")
    private FeeDTO fee;

    @Valid
    private LimitDTO limit;
}