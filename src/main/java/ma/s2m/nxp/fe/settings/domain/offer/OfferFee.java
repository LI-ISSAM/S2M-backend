package ma.s2m.nxp.fe.settings.domain.offer;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.FeeType;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OfferFee {

    @Enumerated(EnumType.STRING)
    @Column(name = "OFR_FEE_TYPE", nullable = false, length = 20)
    private FeeType feeType;

    @Column(name = "OFR_FEE_VALUE", nullable = false)
    private BigDecimal value;
}