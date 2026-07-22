package ma.s2m.nxp.fe.settings.domain.merchant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantAccountRouting {

    @Column(name = "RTG_CMS_ACCOUNT", length = 50)
    private String cmsAccount;

    @Column(name = "RTG_BANK_ACCOUNT", length = 50)
    private String bankAccount;
}