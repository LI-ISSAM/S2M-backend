package ma.s2m.nxp.fe.settings.dto.program;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LimitDTO {

    @NotNull(message = "maxAmountPerTransaction is required")

    private BigDecimal maxAmountPerTransaction;
    @NotNull(message = "maxTotalAmount is required")

    private BigDecimal maxTotalAmount;
    @NotNull(message = "maxMonthlyInstallment is required")

    private BigDecimal maxMonthlyInstallment;

    @Builder.Default
    @NotNull(message = "allowedChannel is required")

    private Set<String> allowedChannels = new HashSet<>(); // POS / ECOM / MOBILE
    @NotNull(message = "Type is required")

    private String mccCode;
}
