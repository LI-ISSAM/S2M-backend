package ma.s2m.nxp.fe.settings.DTO.offer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OfferDTO {

    private Long id;

    @NotBlank(message = "Offer Name is required")
    @Size(min = 2, max = 50, message = "Offer Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Program is required")
    private Long programId;

    /**
     * Champ en lecture seule, renvoyé par le backend pour affichage.
     */
    private String programName;

    @NotNull(message = "Number of Installments is required")
    @Min(value = 1, message = "Number of Installments must be at least 1")
    private Integer numberOfInstallments;

    @NotNull(message = "Start Date is required")
    private LocalDate startDate;

    @NotNull(message = "Status is required")
    private String status; // PENDING / ACTIVE / SUSPENDED / ARCHIVED

    @Builder.Default
    private boolean isDefault = false;

    @Valid
    @NotNull(message = "Fee is required")
    private OfferFeeDTO fee;

    @Builder.Default
    private boolean hasCustomLimit = false;

    @Valid
    private OfferLimitDTO limit;
}