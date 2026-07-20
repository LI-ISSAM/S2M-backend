package ma.s2m.nxp.fe.settings.domain.program;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.FeeType;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Fee {

    @Column(name = "FEE_LABEL", nullable = false, length = 100)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "FEE_TYPE", nullable = false, length = 20)
    private FeeType feeType;

    @Column(name = "FEE_AMOUNT", nullable = false)
    private BigDecimal amount;
}