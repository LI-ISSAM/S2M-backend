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
public class CardFees {

    @Column(name = "CF_PERSONALIZATION_FEES")
    private BigDecimal personalizationFees;

    @Column(name = "CF_MEMBERSHIP_FEE")
    private BigDecimal membershipFee;

    @Column(name = "CF_LAST_DATE")
    private LocalDate lastDate;

    @Column(name = "CF_RENEW_FEE")
    private BigDecimal renewFee;

    @Column(name = "CF_PIN_RECALCUL_FEE")
    private BigDecimal pinRecalculFee;

    @Column(name = "CF_CARD_DESIGN_FEE")
    private BigDecimal cardDesignFee;

    @Column(name = "CF_EXPRESS_DELIVERY_FEE")
    private BigDecimal expressDeliveryFee;
}