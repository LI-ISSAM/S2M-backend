package ma.s2m.nxp.fe.settings.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerLink {

    @Column(name = "LNK_CARD_NUMBER", length = 19)
    private String cardNumber;

    @Column(name = "LNK_MXP_ACCOUNT", length = 50)
    private String mxpAccount;

    @Column(name = "LNK_BANK_ACCOUNT", length = 50)
    private String bankAccount;

    @Column(name = "LNK_CHECKBOOK", length = 10)
    private String checkbook;
}