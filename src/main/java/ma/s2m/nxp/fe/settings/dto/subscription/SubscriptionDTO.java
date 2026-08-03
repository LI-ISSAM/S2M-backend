package ma.s2m.nxp.fe.settings.dto.subscription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SubscriptionDTO {

    private Long id;

    /**
     * Identifiant métier généré par le backend, renvoyé en lecture seule.
     */
    private String subscriptionId;

    @NotNull(message = "Customer is required")
    private Long customerId;
    private String customerEmail;

    @NotNull(message = "Program is required")
    private Long programId;

    private Long offerId;

    @NotNull(message = "Subscription Date is required")
    private LocalDate subscriptionDate;

    @NotNull(message = "Mode is required")
    private String mode; // WITH_KYC / WITHOUT_KYC

    private String status; // ELIGIBLE / ENROLLED / REJECTED

    @Valid
    private EligibilityDTO eligibility;
}