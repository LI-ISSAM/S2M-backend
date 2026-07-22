package ma.s2m.nxp.fe.settings.DTO.merchant;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantCommissionDTO {
    private String commission;
    private LocalDate effectiveDate;
}