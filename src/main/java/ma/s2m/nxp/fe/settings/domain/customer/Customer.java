package ma.s2m.nxp.fe.settings.domain.customer;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.SubBin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Customer (fiche KYC complète), reconstruite pour correspondre au
 * formulaire multi-onglets AddCustomer.vue / UpdateCustomer.vue :
 * Customer Data, Customer Information, Professional Information, Address,
 * Account, Card, Cardholder Routing, Account/Card Link.
 *
 * Convention de nommage : ANGLAIS partout (aligné sur le Récapitulatif et
 * les onglets 2 à 9 du formulaire, qui sont self-consistants). Le tout début
 * de l'onglet 1 (Customer Data) utilise encore des noms FRANÇAIS dans son
 * v-model actuel (ex: v-model="customer.banque") qui ne correspondent à
 * aucune propriété déclarée dans data() ni à ce DTO — c'est un bug frontend
 * à corriger séparément (voir message d'accompagnement).
 */
@Entity
@Table(name = "CUSTOMER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CST_CUSTOMER_ID"),
        @UniqueConstraint(columnNames = "CST_EMAIL")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Customer {

    public static final String CUSTOMER_SEQ_NAME = "CUSTOMER_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CUSTOMER_SEQ_NAME)
    @SequenceGenerator(name = CUSTOMER_SEQ_NAME, sequenceName = CUSTOMER_SEQ_NAME, allocationSize = 1)
    @Column(name = "CST_ID")
    @ToString.Include
    private Long id;

    /** Identifiant métier auto-généré à la création (ex: CST-483920). */
    @Column(name = "CST_CUSTOMER_ID", length = 30, updatable = false)
    @ToString.Include
    private String customerId;

    /**
     * Nom complet dérivé (firstName + middleName + lastName), recalculé et
     * persisté automatiquement à chaque sauvegarde. Conservé pour que les
     * autres modules (Card BNPL, InstallmentPlan, CustomerInstallment) qui
     * recherchent/affichent par "fullName" continuent de fonctionner sans
     * modification.
     */
    @Column(name = "CST_FULL_NAME", length = 250)
    @ToString.Include
    private String fullName;

    // ---------- 1. Customer Data ----------

    @Column(name = "CST_BANK", length = 100)
    private String bank;

    @Column(name = "CST_BRANCH", length = 100)
    private String branch;

    @Column(name = "CST_CLIENT_ID", length = 40)
    private String clientId;

    @Column(name = "CST_VIP_CATEGORY", length = 30)
    private String vipCategory;

    @Column(name = "CST_TITLE", length = 20)
    private String title;

    @Column(name = "CST_FIRST_NAME", length = 80, nullable = false)
    private String firstName;

    @Column(name = "CST_MIDDLE_NAME", length = 80)
    private String middleName;

    @Column(name = "CST_LAST_NAME", length = 80, nullable = false)
    private String lastName;

    @Column(name = "CST_BIRTH_DATE", nullable = false)
    private LocalDate birthDate;

    @Column(name = "CST_BIRTH_PLACE", length = 100)
    private String birthPlace;

    @Column(name = "CST_PRIMARY_ID_TYPE", length = 30, nullable = false)
    private String primaryIdType;

    @Column(name = "CST_PRIMARY_ID", length = 50, nullable = false)
    private String primaryId;

    @Column(name = "CST_SECONDARY_ID_TYPE", length = 30)
    private String secondaryIdType;

    @Column(name = "CST_SECONDARY_ID", length = 50)
    private String secondaryId;

    @Column(name = "CST_GENDER", length = 20, nullable = false)
    private String gender;

    @Column(name = "CST_MARITAL_STATUS", length = 30)
    private String maritalStatus;

    @Column(name = "CST_NATIONALITY", length = 60, nullable = false)
    private String nationality;

    @Column(name = "CST_DEPENDENTS")
    private Integer dependents;

    @Column(name = "CST_PASSPORT_EXPIRY_DATE")
    private LocalDate passportExpiryDate;

    @Column(name = "CST_OWNERS_LIST", length = 200)
    private String ownersList;

    @Column(name = "CST_CUSTOMER_SEGMENT", length = 50)
    private String customerSegment;

    @Column(name = "CST_COMPANY", length = 100)
    private String company;

    @Column(name = "CST_CUSTOMER_CURRENCY", length = 10)
    private String customerCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "CST_SUB_BIN", length = 20, nullable = false)
    private SubBin subBin;


    @Column(name = "CST_IDENTITY_FILE")
    private String identityFile;

    // ---------- 2. Customer Information ----------

    @Column(name = "CST_PARENT_CLIENT", length = 100)
    private String parentClient;

    @Column(name = "CST_PARENT_RELATION", length = 50)
    private String parentRelation;

    /** Date de création "métier" saisie par l'utilisateur, distincte de l'audit createdAt. */
    @Column(name = "CST_BUSINESS_CREATION_DATE")
    private LocalDate customerCreationDate;

    @Column(name = "CST_RESOLVABILITY_LEVEL", length = 50)
    private String resolvabilityLevel;

    @Column(name = "CST_STATUS", length = 30, nullable = false)
    private String status;

    @Column(name = "CST_STATUS_DATE")
    private LocalDate statusDate;

    @Column(name = "CST_STATUS_REASON", length = 100)
    private String statusReason;

    @Column(name = "CST_DEBIT_CARD", length = 10)
    private String debitCard;

    @Column(name = "CST_CREDIT_CARD", length = 10)
    private String creditCard;

    @Column(name = "CST_PREPAID_CARD", length = 10)
    private String prepaidCard;

    @Column(name = "CST_PHONE_NUMBER", length = 30)
    private String phoneNumber;

    @Column(name = "CST_EMAIL", length = 100, nullable = false)
    private String email;

    // ---------- 3. Professional Information ----------

    @Column(name = "CST_EMPLOYEE_CODE", length = 50)
    private String employeeCode;

    @Column(name = "CST_EMPLOYEE_NAME", length = 100)
    private String employeeName;

    @Column(name = "CST_POSITION", length = 100)
    private String position;

    @Column(name = "CST_GROSS_INCOME")
    private BigDecimal grossIncome;

    @Column(name = "CST_NET_INCOME")
    private BigDecimal netIncome;


    @Column(name = "CST_RISK_LEVEL", length = 30)
    private String riskLevel;

    // ---------- 5. Account (valeurs par défaut) ----------

    @Column(name = "CST_DEFAULT_MXP_ACCOUNT", length = 50)
    private String defaultMxpAccount;

    @Column(name = "CST_DEFAULT_BANK_ACCOUNT", length = 50)
    private String defaultBankAccount;

    // ---------- Tableaux (4, 5, 6, 7, 8) ----------

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CUSTOMER_ADDRESSES", joinColumns = @JoinColumn(name = "CST_ID"))
    @Builder.Default
    private List<CustomerAddress> addresses = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CUSTOMER_ACCOUNTS", joinColumns = @JoinColumn(name = "CST_ID"))
    @Builder.Default
    private List<CustomerAccount> accounts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CUSTOMER_CARDS_INFO", joinColumns = @JoinColumn(name = "CST_ID"))
    @Builder.Default
    private List<CustomerCardInfo> cards = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CUSTOMER_ROUTINGS", joinColumns = @JoinColumn(name = "CST_ID"))
    @Builder.Default
    private List<CustomerRouting> routings = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CUSTOMER_LINKS", joinColumns = @JoinColumn(name = "CST_ID"))
    @Builder.Default
    private List<CustomerLink> links = new ArrayList<>();

    // ---------- Audit ----------

    @Column(name = "CREATED_AT", updatable = false)
    private java.time.Instant createdAt;

    @Column(name = "UPDATED_AT")
    private java.time.Instant updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = java.time.Instant.now();
        this.updatedAt = this.createdAt;
        recomputeFullName();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = java.time.Instant.now();
        recomputeFullName();
    }

    private void recomputeFullName() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null && !firstName.isBlank()) sb.append(firstName.trim());
        if (middleName != null && !middleName.isBlank()) sb.append(sb.length() > 0 ? " " : "").append(middleName.trim());
        if (lastName != null && !lastName.isBlank()) sb.append(sb.length() > 0 ? " " : "").append(lastName.trim());
        this.fullName = sb.toString();
    }
}