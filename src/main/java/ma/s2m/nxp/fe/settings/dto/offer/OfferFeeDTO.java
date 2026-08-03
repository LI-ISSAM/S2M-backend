package ma.s2m.nxp.fe.settings.dto.offer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OfferFeeDTO {

    @NotNull(message = "Fee Type is required")
    private String feeType; // PERCENTAGE / FIXED

    @NotNull(message = "Fee Value is required")
    @Min(value = 0, message = "Fee Value must be positive")
    private BigDecimal value;
}