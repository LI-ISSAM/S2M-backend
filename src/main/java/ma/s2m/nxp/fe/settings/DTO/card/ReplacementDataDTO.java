package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReplacementDataDTO {
    private LocalDate replacementDate;
    private String replacementCardNumber;
    private String cardName;
    private LocalDate newEffectiveDate;
    private String newExpiryDate;         // format MM/YY
    private LocalDate newPreparationDate;
    private String replacementOldExpiryDate; // format MM/YY
    private String replacementStatus;     // IN_INSTANCE / COMPLETED / CANCELLED
    private BigDecimal cardReplacementFee;
    private String replacementReason;
    private String replacementPinGeneration; // YES / NO
    private BigDecimal pinRecalculationFee;
}