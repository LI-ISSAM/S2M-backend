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
public class RenewData {

    @Column(name = "RND_RENEW_TYPE", length = 20)
    private String renewType;

    @Column(name = "RND_RENEW_DATE")
    private LocalDate renewDate;

    @Column(name = "RND_CARD_RENEW_STATUS", length = 20)
    private String cardRenewStatus;

    @Column(name = "RND_NEW_EFFECTIVE_DATE")
    private LocalDate newEffectiveDate;

    @Column(name = "RND_NEW_PREPARATION_DATE")
    private LocalDate newPreparationDate;

    @Column(name = "RND_EXPIRY_DATE", length = 5)
    private String expiryDate;

    @Column(name = "RND_OLD_EXPIRY_DATE", length = 5)
    private String oldExpiryDate;

    @Column(name = "RND_RENEW_PIN_GENERATION", length = 10)
    private String renewPinGeneration;

    @Column(name = "RND_RENEW_MANUAL_GENERATION", length = 100)
    private String renewManualGeneration;

    @Column(name = "RND_RENEW_FEE")
    private BigDecimal renewFee;

    @Column(name = "RND_RENEW_PIN_FEE")
    private BigDecimal renewPinFee;
}