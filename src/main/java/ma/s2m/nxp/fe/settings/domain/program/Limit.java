package ma.s2m.nxp.fe.settings.domain.program;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Limit {

    @Column(name = "LMT_MAX_AMOUNT_PER_TRANSACTION")
    private BigDecimal maxAmountPerTransaction;

    @Column(name = "LMT_MAX_TOTAL_AMOUNT")
    private BigDecimal maxTotalAmount;

    @Column(name = "LMT_MAX_MONTHLY_INSTALLMENT")
    private BigDecimal maxMonthlyInstallment;

    @Column(name = "LMT_MCC_CODE", length = 10)
    private String mccCode;
}