package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CardInfoDTO {
    private LocalDate statusDate;
    private String primarySecondary; // PRIMARY / SECONDARY
    private String primaryCard;
    private LocalDate startDate;
    private Integer lifeCycleYears;
    private Integer pinTryLimit;
    private Integer pinTryCount;
    private String oppositionStatus;
    private String reason;
}