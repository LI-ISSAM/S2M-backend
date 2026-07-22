package ma.s2m.nxp.fe.settings.DTO.card;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerDataDTO {
    private String bank;
    private String title;       // MR / MRS / MS
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;      // MALE / FEMALE
}
