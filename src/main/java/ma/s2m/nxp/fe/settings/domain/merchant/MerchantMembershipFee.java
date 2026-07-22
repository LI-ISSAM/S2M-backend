package ma.s2m.nxp.fe.settings.domain.merchant;

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
public class MerchantMembershipFee {

    @Column(name = "FEE_NAME", length = 100)
    private String name;

    @Column(name = "FEE_AMOUNT")
    private BigDecimal amount;

    @Column(name = "FEE_PERIODICITY", length = 20)
    private String periodicity;

    @Column(name = "FEE_DATE")
    private LocalDate date;
}