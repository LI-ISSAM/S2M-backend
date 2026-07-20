package ma.s2m.nxp.fe.settings.DTO.installmentplan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstallmentPlanDTO {

    private Long id;

    @NotNull(message = "Customer is required")
    private Long customerId;

    /**
     * Champ en lecture seule, renvoyé par le backend pour affichage.
     */
    private String customerName;

    private Long offerId; // optionnel

    private String offerName;

    @NotNull(message = "Total Amount is required")
    @Min(value = 0, message = "Total Amount must be positive")
    private BigDecimal totalAmount;

    @NotNull(message = "Number of Installments is required")
    @Min(value = 1, message = "Number of Installments must be at least 1")
    private Integer numberOfInstallments;

    @NotNull(message = "Start Date is required")
    private LocalDate startDate;

    @NotNull(message = "Status is required")
    private String status; // PENDING / ACTIVE / SUSPENDED / ARCHIVED

    /**
     * Échéancier généré par le backend, renvoyé en lecture seule.
     */
    @Builder.Default
    private List<InstallmentDTO> installments = new ArrayList<>();
}