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
public class CustomerRouting {

    @Column(name = "RTG_CARD_NUMBER", length = 19)
    private String cardNumber;

    @Column(name = "RTG_MXP_ACCOUNT", length = 50)
    private String mxpAccount;

    @Column(name = "RTG_BANK_ACCOUNT", length = 50)
    private String bankAccount;
}