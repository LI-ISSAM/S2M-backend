package ma.s2m.nxp.fe.settings.DTO.program;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EligibilityDTO {

    @NotNull(message = "Min Age is required")
    @Min(value = 0, message = "Min Age must be positive")
    private Integer minAge;

    @NotNull(message = "Max Age is required")
    @Max(value = 120, message = "Max Age must be at most 120")
    private Integer maxAge;

    @NotNull(message = "Min Salary is required")
    @Min(value = 0, message = "Min Salary must be positive")
    private BigDecimal minSalary;

    @Builder.Default
    private Set<String> allowedSubBins = new HashSet<>();
}