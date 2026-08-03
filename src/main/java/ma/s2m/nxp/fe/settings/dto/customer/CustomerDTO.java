package ma.s2m.nxp.fe.settings.dto.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerDTO {

    private Long id;

    /** Généré par le backend, ignoré si envoyé à la création/update. */
    private String customerId;

    /** Dérivé (firstName + middleName + lastName), renvoyé en lecture seule. */
    private String fullName;

    // ---------- 1. Customer Data ----------

    private String bank;
    private String branch;
    private String clientId;
    private String vipCategory;
    private String title;

    @NotBlank(message = "First Name is required")
    @Size(min = 2, max = 80, message = "First Name must be between 2 and 80 characters")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 2, max = 80, message = "Last Name must be between 2 and 80 characters")
    private String lastName;

    @NotNull(message = "Birth Date is required")
    private LocalDate birthDate;

    private String birthPlace;

    @NotBlank(message = "Primary ID Type is required")
    private String primaryIdType;

    @NotBlank(message = "Primary ID is required")
    private String primaryId;

    private String secondaryIdType;
    private String secondaryId;

    @NotBlank(message = "Gender is required")
    private String gender;

    private String maritalStatus;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @Min(value = 0, message = "Dependents must be positive")
    private Integer dependents;

    private LocalDate passportExpiryDate;
    private String ownersList;
    private String customerSegment;
    private String company;
    private String customerCurrency;

    @NotBlank(message = "SubBin is required")
    private String subBin; // VISA / MASTERCARD / AMEX / DISCOVER

    private String identityFile;

    // ---------- 2. Customer Information ----------

    private String parentClient;
    private String parentRelation;
    private LocalDate creationDate;
    private String resolvabilityLevel;

    @NotBlank(message = "Status is required")
    private String status;

    private LocalDate statusDate;
    private String statusReason;
    private String debitCard;
    private String creditCard;
    private String prepaidCard;
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    // ---------- 3. Professional Information ----------

    private String employeeCode;
    private String employeeName;
    private String position;

    @Min(value = 0, message = "Gross Income must be positive")
    private BigDecimal grossIncome;

    @Min(value = 0, message = "Net Income must be positive")
    private BigDecimal netIncome;


    private String riskLevel;

    // ---------- 5. Account (valeurs par défaut) ----------

    private String defaultMxpAccount;
    private String defaultBankAccount;

    // ---------- Tableaux ----------

    @Valid
    @Builder.Default
    private List<CustomerAddressDTO> addresses = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CustomerAccountDTO> accounts = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CustomerCardInfoDTO> cards = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CustomerRoutingDTO> routings = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<CustomerLinkDTO> links = new ArrayList<>();
}