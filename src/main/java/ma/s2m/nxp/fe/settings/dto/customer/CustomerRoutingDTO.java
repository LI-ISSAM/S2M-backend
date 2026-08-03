package ma.s2m.nxp.fe.settings.dto.customer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerRoutingDTO {
    private String cardNumber;
    private String mxpAccount;
    private String bankAccount;
}