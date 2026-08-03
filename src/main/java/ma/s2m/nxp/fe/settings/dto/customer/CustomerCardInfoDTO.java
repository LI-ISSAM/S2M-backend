package ma.s2m.nxp.fe.settings.dto.customer;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerCardInfoDTO {
    private String cardNumber;
    private String cardName;
    private String expirationDate;
    private String autoRenewal;
    private LocalDate lastTransactionDate;
    private String type;
    private String product;
    private String status;
}