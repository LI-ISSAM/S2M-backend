package ma.s2m.nxp.fe.settings.domain.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SubscriptionEligibility {

    @Column(name = "SUB_ELIG_AGE_OK")
    private Boolean ageOk;

    @Column(name = "SUB_ELIG_SALARY_OK")
    private Boolean salaryOk;

    @Column(name = "SUB_ELIG_SUBBIN_OK")
    private Boolean subBinOk;

    @Column(name = "SUB_ELIG_ELIGIBLE")
    private Boolean eligible;
}