package ma.s2m.nxp.fe.settings.DTO.merchant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class MerchantDTO {

    private Long id;

    @NotBlank(message = "Merchant Name is required")
    @Size(min = 2, max = 50, message = "Merchant Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Reference is required")
    @Size(min = 2, max = 30, message = "Reference must be between 2 and 30 characters")
    private String reference;

    @NotBlank(message = "MCC Code is required")
    private String mccCode;

    @NotNull(message = "Institution is required")
    private Long institutionId;

    private String institutionName;

    @NotNull(message = "Type is required")
    private String type;

    @NotNull(message = "Status is required")
    private String status;

    // ---------- Merchant Data ----------
    private String merchantId;
    private String corporateName;
    private String dbaName;
    private String city;
    private String branch;
    private String bank;
    private String identityFile;

    // ---------- Merchant Information ----------
    private String category;
    private String parentGroup;
    private String solvability;
    private String businessType;
    private String contractNumber;
    private LocalDate signatureDate;
    private LocalDate creationDate;
    private LocalDate latePaymentDate;
    private LocalDate statusDate;
    private String oppositionStatus;

    // ---------- Merchant Identity ----------
    private String licence;
    private String siretNumber;
    private String fiscalIdentityNumber;
    private String commercialRegisterNumber;
    private String socialSecurityNumber;
    private BigDecimal capital;

    // ---------- Merchant Parameters ----------
    private String mccGroup;
    private String merchantGroup;
    private String merchantProgram;
    private String riskManagementGroup;
    private String paymentMode;
    private String periodicity;
    private String checkbookName;
    private String allAccount;
    private String dsDecision;
    private String dsChallenge;

    // ---------- Merchant Currency ----------
    private String defaultCurrency;

    // ---------- Account Routing (valeurs par défaut) ----------
    private String defaultMxpAccount;
    private String defaultBankAccount;

    // ---------- Merchant Statement ----------
    private String frequency;
    private String period;
    private String support;
    private LocalDate lastStatementDate;

    // ---------- Tableaux ----------
    @Valid
    @Builder.Default
    private List<MerchantOwnerDTO> owners = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<MerchantCurrencySupportedDTO> currencySupported = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<MerchantAccountDTO> accounts = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<MerchantAccountRoutingDTO> accountRoutings = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<MerchantMembershipFeeDTO> membershipFees = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<MerchantCommissionDTO> commissions = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<MerchantAddressDTO> addresses = new ArrayList<>();
}