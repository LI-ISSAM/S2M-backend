package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RenewDataDTO {
    private String renewType;          // AUTOMATIC / MANUAL
    private LocalDate renewDate;
    private String cardRenewStatus;    // IN_INSTANCE / COMPLETED / CANCELLED
    private LocalDate newEffectiveDate;
    private LocalDate newPreparationDate;
    private String expiryDate;         // format MM/YY
    private String oldExpiryDate;      // format MM/YY
    private String renewPinGeneration; // YES / NO
    private String renewManualGeneration;
    private BigDecimal renewFee;
    private BigDecimal renewPinFee;
}