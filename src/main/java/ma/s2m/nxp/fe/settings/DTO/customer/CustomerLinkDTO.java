package ma.s2m.nxp.fe.settings.DTO.customer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerLinkDTO {
    private String cardNumber;
    private String mxpAccount;
    private String bankAccount;
    private String checkbook;
}