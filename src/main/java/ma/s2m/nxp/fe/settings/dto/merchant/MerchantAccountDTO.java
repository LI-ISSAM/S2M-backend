package ma.s2m.nxp.fe.settings.dto.merchant;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantAccountDTO {
    private String cmsAccount;
    private String account;
    private String currency;
    private String status;
    private LocalDate statusDate;
    private String branch;
}