package ma.s2m.nxp.fe.settings.domain.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReplacementData {

    @Column(name = "RPD_REPLACEMENT_DATE")
    private LocalDate replacementDate;

    @Column(name = "RPD_REPLACEMENT_CARD_NUMBER", length = 19)
    private String replacementCardNumber;

    @Column(name = "RPD_CARD_NAME", length = 80)
    private String cardName;

    @Column(name = "RPD_NEW_EFFECTIVE_DATE")
    private LocalDate newEffectiveDate;

    @Column(name = "RPD_NEW_EXPIRY_DATE", length = 5)
    private String newExpiryDate;

    @Column(name = "RPD_NEW_PREPARATION_DATE")
    private LocalDate newPreparationDate;

    @Column(name = "RPD_OLD_EXPIRY_DATE", length = 5)
    private String replacementOldExpiryDate;

    @Column(name = "RPD_REPLACEMENT_STATUS", length = 20)
    private String replacementStatus;

    @Column(name = "RPD_CARD_REPLACEMENT_FEE")
    private BigDecimal cardReplacementFee;

    @Column(name = "RPD_REPLACEMENT_REASON", length = 200)
    private String replacementReason;

    @Column(name = "RPD_REPLACEMENT_PIN_GENERATION", length = 10)
    private String replacementPinGeneration;

    @Column(name = "RPD_PIN_RECALCULATION_FEE")
    private BigDecimal pinRecalculationFee;
}