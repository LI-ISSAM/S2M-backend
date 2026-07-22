package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AdditionalDataDTO {
    private String firstCodeService;
    private String secondCodeService;
    private String thirdCodeService;
    private String internetOrder;   // YES / NO
    private String mailOrder;       // YES / NO
    private String chipFlag;        // YES / NO
    private String magneticFlag;    // YES / NO
    private String pinGeneration;   // YES / NO
    private LocalDate lastTransactionDate;
    private String anonymousCard;   // YES / NO
    private String pinMethod;       // PVV / IBM
    private String newCardDesign;   // YES / NO
}