package ma.s2m.nxp.fe.settings.dto.operation;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OperationDTO {

    private Long id;

    @NotBlank(message = "PAN is required")
    private String pan;

    @NotBlank(message = "Issuing Bank is required")
    private String issuingBank;

    @NotBlank(message = "Acquiring is required")
    private String acquiring;

    @NotBlank(message = "RRN is required")
    private String rrn;

    @NotBlank(message = "STAN is required")
    private String stan;

    @NotNull(message = "Merchant is required")
    private Long merchantId;

    /** Champ en lecture seule, renvoyé par le backend pour affichage. */
    private String merchantReference;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Transaction Time is required")
    private LocalDate transactionTime;

    @NotNull(message = "BNPL Program is required")
    private Long bnplProgramId;

    private String bnplProgramName;

    /**
     * Envoyé par le frontend en clair (email), pas un id — résolu côté
     * service. Renvoyé tel quel en lecture (matche exactement ce que le
     * Récapitulatif et OperationSpace.vue attendent).
     */
    @NotBlank(message = "Customer Email is required")
    @Email(message = "Customer Email is invalid")
    private String customerEmail;

    private Long customerId;

    private Integer numberOfInstallments;
}