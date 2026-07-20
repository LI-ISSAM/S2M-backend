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
public class CustomerContact {

    @Column(name = "CST_EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "CST_PHONE", length = 30, nullable = false)
    private String phone;

    @Column(name = "CST_ADDRESS", length = 200)
    private String address;

    @Column(name = "CST_CITY", length = 100)
    private String city;

    @Column(name = "CST_COUNTRY", length = 100)
    private String country;
}