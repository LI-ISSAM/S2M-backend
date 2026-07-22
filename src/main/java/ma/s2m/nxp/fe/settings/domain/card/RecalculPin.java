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
public class RecalculPin {

    @Column(name = "RCP_PIN_RECALCULATION_DATE")
    private LocalDate pinRecalculationDate;

    @Column(name = "RCP_PIN_RECALCULATION_FEE")
    private BigDecimal pinRecalculationFee;
}



