package ma.s2m.nxp.fe.settings.domain.merchant;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.MerchantStatus;
import ma.s2m.nxp.fe.settings.enums.MerchantType;
import ma.s2m.nxp.fe.settings.domain.member.Institution;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Merchant enrichie : Merchant Data, Merchant Information, Merchant
 * Identity, Merchant Owners, Merchant Parameters, Merchant Currency, Account,
 * Account Routing, Commission/Fees, Address, Merchant Statement.
 */
@Entity
@Table(name = "MERCHANT", uniqueConstraints = {
        @UniqueConstraint(columnNames = "MER_REFERENCE")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Merchant {

    public static final String MERCHANT_SEQ_NAME = "MERCHANT_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MERCHANT_SEQ_NAME)
    @SequenceGenerator(name = MERCHANT_SEQ_NAME, sequenceName = MERCHANT_SEQ_NAME, allocationSize = 1)
    @Column(name = "MER_ID")
    @ToString.Include
    private Long id;

    @Column(name = "MER_NAME", length = 50, nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "MER_REFERENCE", length = 30, nullable = false)
    @ToString.Include
    private String reference;

    @Column(name = "MER_MCC_CODE", length = 10, nullable = false)
    private String mccCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INST_ID", nullable = false)
    private Institution institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "MER_TYPE", length = 20, nullable = false)
    private MerchantType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "MER_STATUS", length = 20, nullable = false)
    private MerchantStatus status;

    // ---------- Merchant Data ----------

    @Column(name = "MER_MERCHANT_ID", length = 40)
    private String merchantId;

    @Column(name = "MER_CORPORATE_NAME", length = 150)
    private String corporateName;

    @Column(name = "MER_DBA_NAME", length = 150)
    private String dbaName;

    @Column(name = "MER_CITY", length = 100)
    private String city;

    @Column(name = "MER_BRANCH", length = 100)
    private String branch;

    @Column(name = "MER_BANK", length = 100)
    private String bank;

    @Column(name = "MER_IDENTITY_FILE")
    private String identityFile;

    // ---------- Merchant Information ----------

    @Column(name = "MER_CATEGORY", length = 20)
    private String category;

    @Column(name = "MER_PARENT_GROUP", length = 100)
    private String parentGroup;

    @Column(name = "MER_SOLVABILITY", length = 100)
    private String solvability;

    @Column(name = "MER_BUSINESS_TYPE", length = 20)
    private String businessType;

    @Column(name = "MER_CONTRACT_NUMBER", length = 50)
    private String contractNumber;

    @Column(name = "MER_SIGNATURE_DATE")
    private LocalDate signatureDate;

    /** Date de création "métier", distincte de l'audit createdAt. */
    @Column(name = "MER_BUSINESS_CREATION_DATE")
    private LocalDate businessCreationDate;

    @Column(name = "MER_LATE_PAYMENT_DATE")
    private LocalDate latePaymentDate;

    @Column(name = "MER_STATUS_DATE")
    private LocalDate statusDate;

    @Column(name = "MER_OPPOSITION_STATUS", length = 10)
    private String oppositionStatus;

    // ---------- Merchant Identity ----------

    @Column(name = "MER_LICENCE", length = 100)
    private String licence;

    @Column(name = "MER_SIRET_NUMBER", length = 50)
    private String siretNumber;

    @Column(name = "MER_FISCAL_IDENTITY_NUMBER", length = 50)
    private String fiscalIdentityNumber;

    @Column(name = "MER_COMMERCIAL_REGISTER_NUMBER", length = 50)
    private String commercialRegisterNumber;

    @Column(name = "MER_SOCIAL_SECURITY_NUMBER", length = 50)
    private String socialSecurityNumber;

    @Column(name = "MER_CAPITAL")
    private BigDecimal capital;

    // ---------- Merchant Parameters ----------

    @Column(name = "MER_MCC_GROUP", length = 50)
    private String mccGroup;

    @Column(name = "MER_MERCHANT_GROUP", length = 50)
    private String merchantGroup;

    @Column(name = "MER_MERCHANT_PROGRAM", length = 50)
    private String merchantProgram;

    @Column(name = "MER_RISK_MANAGEMENT_GROUP", length = 50)
    private String riskManagementGroup;

    @Column(name = "MER_PAYMENT_MODE", length = 20)
    private String paymentMode;

    @Column(name = "MER_PERIODICITY", length = 20)
    private String periodicity;

    @Column(name = "MER_CHECKBOOK_NAME", length = 100)
    private String checkbookName;

    @Column(name = "MER_ALL_ACCOUNT", length = 10)
    private String allAccount;

    @Column(name = "MER_3DS_DECISION", length = 50)
    private String dsDecision;

    @Column(name = "MER_3DS_CHALLENGE", length = 50)
    private String dsChallenge;

    // ---------- Merchant Currency ----------

    @Column(name = "MER_DEFAULT_CURRENCY", length = 10)
    private String defaultCurrency;

    // ---------- Account Routing (valeurs par défaut) ----------

    @Column(name = "MER_DEFAULT_MXP_ACCOUNT", length = 50)
    private String defaultMxpAccount;

    @Column(name = "MER_DEFAULT_BANK_ACCOUNT", length = 50)
    private String defaultBankAccount;

    // ---------- Merchant Statement ----------

    @Column(name = "MER_FREQUENCY", length = 20)
    private String frequency;

    @Column(name = "MER_PERIOD", length = 20)
    private String period;

    @Column(name = "MER_SUPPORT", length = 20)
    private String support;

    @Column(name = "MER_LAST_STATEMENT_DATE")
    private LocalDate lastStatementDate;

    // ---------- Tableaux ----------

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_OWNERS", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantOwner> owners = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_CURRENCIES", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantCurrencySupported> currencySupported = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_ACCOUNTS", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantAccount> accounts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_ACCOUNT_ROUTINGS", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantAccountRouting> accountRoutings = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_MEMBERSHIP_FEES", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantMembershipFee> membershipFees = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_COMMISSIONS", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantCommission> commissions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MERCHANT_ADDRESSES", joinColumns = @JoinColumn(name = "MER_ID"))
    @Builder.Default
    private List<MerchantAddress> addresses = new ArrayList<>();

    // ---------- Audit ----------

    @Column(name = "CREATED_AT", updatable = false)
    private java.time.Instant createdAt;

    @Column(name = "UPDATED_AT")
    private java.time.Instant updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = java.time.Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = java.time.Instant.now();
    }
}