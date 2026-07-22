package ma.s2m.nxp.fe.settings.DTO.merchant;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MerchantAddressDTO {
    private String addressType;
    private String address;
    private String address2;
    private String city;
    private String phone;
    private String fax;
}