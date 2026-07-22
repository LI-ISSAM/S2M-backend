package ma.s2m.nxp.fe.settings.domain.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerData {

    @Column(name = "CD_BANK", length = 100)
    private String bank;

    @Column(name = "CD_TITLE", length = 20)
    private String title;

    @Column(name = "CD_FIRST_NAME", length = 80)
    private String firstName;

    @Column(name = "CD_MIDDLE_NAME", length = 80)
    private String middleName;

    @Column(name = "CD_LAST_NAME", length = 80)
    private String lastName;

    @Column(name = "CD_GENDER", length = 20)
    private String gender;
}











