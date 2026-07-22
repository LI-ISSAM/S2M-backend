package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RecalculPinDTO {
    private LocalDate pinRecalculationDate;
    private BigDecimal pinRecalculationFee;
}