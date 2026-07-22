package ma.s2m.nxp.fe.settings.domain.merchant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantCommission {

    @Column(name = "COM_COMMISSION", length = 100)
    private String commission;

    @Column(name = "COM_EFFECTIVE_DATE")
    private LocalDate effectiveDate;
}