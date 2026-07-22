package ma.s2m.nxp.fe.settings.domain.merchant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantAccount {

    @Column(name = "ACC_CMS_ACCOUNT", length = 50)
    private String cmsAccount;

    @Column(name = "ACC_ACCOUNT", length = 50)
    private String account;

    @Column(name = "ACC_CURRENCY", length = 10)
    private String currency;

    @Column(name = "ACC_STATUS", length = 20)
    private String status;

    @Column(name = "ACC_STATUS_DATE")
    private LocalDate statusDate;

    @Column(name = "ACC_BRANCH", length = 100)
    private String branch;
}