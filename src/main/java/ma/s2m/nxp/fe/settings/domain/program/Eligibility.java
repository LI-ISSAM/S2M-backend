package ma.s2m.nxp.fe.settings.domain.program;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Eligibility {

    @Column(name = "ELIG_MIN_AGE", nullable = false)
    private Integer minAge;

    @Column(name = "ELIG_MAX_AGE", nullable = false)
    private Integer maxAge;

    @Column(name = "ELIG_MIN_SALARY", nullable = false)
    private BigDecimal minSalary;
}