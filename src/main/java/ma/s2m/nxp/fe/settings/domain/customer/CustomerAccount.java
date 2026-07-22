package ma.s2m.nxp.fe.settings.domain.customer;

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
public class CustomerAccount {

    @Column(name = "MXP_ACCOUNT", length = 50)
    private String mxpAccount;

    @Column(name = "BANK_ACCOUNT", length = 50)
    private String bankAccount;

    @Column(name = "ACC_CREATION_DATE")
    private LocalDate creationDate;

    @Column(name = "CURRENCY", length = 10)
    private String currency;

    @Column(name = "ACCOUNT_TYPE", length = 30)
    private String accountType;

    @Column(name = "ACC_STATUS", length = 20)
    private String status;

    @Column(name = "ACC_STATUS_DATE")
    private LocalDate statusDate;
}