package ma.s2m.nxp.fe.settings.dto.installment_plan;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstallmentDTO {

    private Integer number;

    private LocalDate dueDate;

    private BigDecimal amount;

    private String status;
}