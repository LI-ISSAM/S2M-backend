package ma.s2m.nxp.fe.settings.DTO.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CardDTO {

    private Long id;

    @NotBlank(message = "Card Number is required")
    private String cardNumber;

    @NotBlank(message = "Name On Card is required")
    private String nameOnCard;

    @NotNull(message = "Customer is required")
    private Long customerId;

    /**
     * Champs en lecture seule, renvoyés par le backend pour affichage.
     */
    private String customerName;

    private String customerEmail;

    private Long programId; // optionnel

    private String programName;

    @NotNull(message = "Type is required")
    private String type; // PRE_PAID / DEBIT / CREDIT

    @NotNull(message = "Status is required")
    private String status; // ACTIVE / BLOCKED / DISABLED

    @NotBlank(message = "Expiry Date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiry Date is invalid (format MM/YY)")
    private String expiryDate;

    private String branch;

    /**
     * Renvoyée en lecture seule par le backend (dérivée de l'audit createdAt).
     * C'est ce champ qui manquait côté API, d'où l'affichage vide "-" observé
     * sur CardDetails.vue (card.creationDate).
     */
    private LocalDate creationDate;

    // ---------- Groupes étendus (tous optionnels) ----------

    private CustomerDataDTO customerData;

    private CardInfoDTO cardInfo;

    private AdditionalDataDTO additionalData;

    private CommissionDTO commission;

    private CardFeesDTO cardFees;

    private ReplacementDataDTO replacementData;

    private RenewDataDTO renewData;

    private RecalculPinDTO recalculPin;

    private PersonalizationDataDTO personalizationData;
}