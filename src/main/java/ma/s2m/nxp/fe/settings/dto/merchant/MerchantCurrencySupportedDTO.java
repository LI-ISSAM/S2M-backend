package ma.s2m.nxp.fe.settings.dto.merchant;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantCurrencySupportedDTO {
    private String currency;
}