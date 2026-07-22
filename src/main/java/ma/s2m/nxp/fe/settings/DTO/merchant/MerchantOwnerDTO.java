package ma.s2m.nxp.fe.settings.DTO.merchant;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantOwnerDTO {
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String function;
    private LocalDate birthDate;
    private String location;
}
