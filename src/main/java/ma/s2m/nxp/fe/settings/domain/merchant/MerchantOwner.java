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
public class MerchantOwner {

    @Column(name = "OWR_TITLE", length = 20)
    private String title;

    @Column(name = "OWR_FIRST_NAME", length = 80)
    private String firstName;

    @Column(name = "OWR_MIDDLE_NAME", length = 80)
    private String middleName;

    @Column(name = "OWR_LAST_NAME", length = 80)
    private String lastName;

    @Column(name = "OWR_FUNCTION", length = 100)
    private String function;

    @Column(name = "OWR_BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "OWR_LOCATION", length = 100)
    private String location;
}



