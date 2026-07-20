package ma.s2m.nxp.fe.settings.domain.offer;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.Channel;

import java.math.BigDecimal;

/**
 * Note : contrairement à Program.Limit (qui gère plusieurs allowedChannels via
 * multiselect), l'offre ne gère qu'un seul canal, conformément au formulaire
 * frontend actuel (type="select" sur offer.limit.allowedChannel).
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OfferLimit {

    @Column(name = "OFR_LMT_MAX_AMOUNT_PER_TRANSACTION")
    private BigDecimal maxAmountPerTransaction;

    @Column(name = "OFR_LMT_MAX_TOTAL_AMOUNT")
    private BigDecimal maxTotalAmount;

    @Column(name = "OFR_LMT_MAX_MONTHLY_INSTALLMENT")
    private BigDecimal maxMonthlyInstallment;

    @Enumerated(EnumType.STRING)
    @Column(name = "OFR_LMT_ALLOWED_CHANNEL", length = 20)
    private Channel allowedChannel;

    @Column(name = "OFR_LMT_MCC_CODE", length = 10)
    private String mccCode;
}
