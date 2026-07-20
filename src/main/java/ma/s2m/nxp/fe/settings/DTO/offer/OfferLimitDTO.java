package ma.s2m.nxp.fe.settings.DTO.offer;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OfferLimitDTO {

    private BigDecimal maxAmountPerTransaction;

    private BigDecimal maxTotalAmount;

    private BigDecimal maxMonthlyInstallment;

    private String allowedChannel; // POS / ECOM / MOBILE

    private String mccCode;
}