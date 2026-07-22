package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CardFeesDTO {
    private BigDecimal personalizationFees;
    private BigDecimal membershipFee;
    private LocalDate lastDate;
    private BigDecimal renewFee;
    private BigDecimal pinRecalculFee;
    private BigDecimal cardDesignFee;
    private BigDecimal expressDeliveryFee;
}