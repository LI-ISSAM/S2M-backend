package ma.s2m.nxp.fe.settings.DTO.customer;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerAccountDTO {
    private String mxpAccount;
    private String bankAccount;
    private LocalDate creationDate;
    private String currency;
    private String accountType;
    private String status;
    private LocalDate statusDate;
}