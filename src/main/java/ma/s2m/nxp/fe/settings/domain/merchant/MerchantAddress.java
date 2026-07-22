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
public class MerchantAddress {

    @Column(name = "ADDRESS_TYPE", length = 30)
    private String addressType;

    @Column(name = "ADDRESS", length = 200)
    private String address;

    @Column(name = "ADDRESS_2", length = 200)
    private String address2;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "PHONE", length = 30)
    private String phone;

    @Column(name = "FAX", length = 30)
    private String fax;
}