package ma.s2m.nxp.fe.settings.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Sous-objet Contact, embarqué dans Institution.
 * Correspond exactement au tab "Contact" du formulaire frontend.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Contact {

    @Column(name = "CNT_EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "CNT_PHONE", length = 30, nullable = false)
    private String phone;

    @Column(name = "CNT_ADDRESS", length = 200)
    private String address;

    @Column(name = "CNT_CITY", length = 100)
    private String city;

    @Column(name = "CNT_COUNTRY", length = 100)
    private String country;

    @Column(name = "CNT_WEBSITE", length = 50)
    private String website;
}