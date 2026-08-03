package ma.s2m.nxp.fe.settings.dto.subscription;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EligibilityDTO {
    private Boolean ageOk;
    private Boolean salaryOk;
    private Boolean subBinOk;
    private Boolean eligible;
}