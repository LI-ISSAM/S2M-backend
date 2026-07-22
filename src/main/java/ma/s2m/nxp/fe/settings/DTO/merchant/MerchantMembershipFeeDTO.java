package ma.s2m.nxp.fe.settings.DTO.merchant;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantMembershipFeeDTO {
    private String name;
    private BigDecimal amount;
    private String periodicity;
    private LocalDate date;
}