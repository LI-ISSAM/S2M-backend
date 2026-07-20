package ma.s2m.nxp.fe.settings.DTO.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerDTO {

    private Long id;

    /**
     * Identifiant métier généré par le backend, renvoyé en lecture seule
     * (ignoré si envoyé à la création/update).
     */
    private String customerId;

    @NotBlank(message = "Full Name is required")
    @Size(min = 2, max = 80, message = "Full Name must be between 2 and 80 characters")
    private String fullName;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 120, message = "Age must be at most 120")
    private Integer age;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be positive")
    private BigDecimal salary;

    @NotNull(message = "SubBin is required")
    private String subBin; // VISA / MASTERCARD / AMEX / DISCOVER

    private String photo;

    @Valid
    @NotNull(message = "Contact is required")
    private CustomerContactDTO contact;
}