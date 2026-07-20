package ma.s2m.nxp.fe.settings.DTO.program;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FeeDTO {

    @NotBlank(message = "Fee Label is required")
    private String label;

    @NotNull(message = "Fee Type is required")
    private String feeType; // PERCENTAGE / FIXED

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;
}