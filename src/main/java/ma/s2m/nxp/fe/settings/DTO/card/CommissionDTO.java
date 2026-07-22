package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommissionDTO {
    private String commission;
    private LocalDate effectiveDate;
    private String onlineOffline; // ONLINE / OFFLINE
}