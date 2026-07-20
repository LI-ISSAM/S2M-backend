package ma.s2m.nxp.fe.settings.dto.installment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerInstallmentDTO {

    private Long id;

    @NotNull(message = "Customer is required")
    private Long customerId;

    /**
     * Champs en lecture seule, renvoyés par le backend pour affichage
     * (le frontend en a besoin pour la recherche par nom/email et l'affichage).
     */
    private String customerName;

    private String customerEmail;

    @NotNull(message = "Due Date is required")
    private LocalDate dueDate;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Status is required")
    private String status; // PENDING / PAID / LATE / CANCELLED
}