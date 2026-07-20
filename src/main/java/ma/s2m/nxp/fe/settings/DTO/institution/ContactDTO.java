package ma.s2m.nxp.fe.settings.DTO.institution;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ContactDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    private String city;

    private String country;

    @Size(max = 50, message = "Website must be at most 50 characters")
    private String website;
}