package ma.s2m.nxp.fe.settings.domain.operation;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.merchant.Merchant;
import ma.s2m.nxp.fe.settings.domain.program.Program;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entité Operation (transaction carte évaluée pour paiement BNPL).
 * Note : le frontend envoie "bnplProgramId" (relation vers Program) et
 * "customerEmail" en clair (pas de customerId) — le client est résolu par
 * email côté service. isDefault/programId mentionnés dans
 * OperationService.js#getDefaultOperation() semblent être du code résiduel
 * copié depuis le module Offer et ne correspondent à aucun champ réellement
 * soumis par AddOperation.vue/UpdateOperation.vue ; ils ne sont donc PAS
 * repris ici (voir message d'accompagnement).
 */
@Entity
@Table(name = "OPERATION")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Operation {

    public static final String OPERATION_SEQ_NAME = "OPERATION_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = OPERATION_SEQ_NAME)
    @SequenceGenerator(name = OPERATION_SEQ_NAME, sequenceName = OPERATION_SEQ_NAME, allocationSize = 1)
    @Column(name = "OPR_ID")
    @ToString.Include
    private Long id;

    @Column(name = "OPR_PAN", length = 25, nullable = false)
    private String pan;

    @Column(name = "OPR_ISSUING_BANK", length = 100, nullable = false)
    private String issuingBank;

    @Column(name = "OPR_ACQUIRING", length = 100, nullable = false)
    private String acquiring;

    @Column(name = "OPR_RRN", length = 30, nullable = false)
    @ToString.Include
    private String rrn;

    @Column(name = "OPR_STAN", length = 20, nullable = false)
    private String stan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MER_ID", nullable = false)
    private Merchant merchant;

    @Column(name = "OPR_AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "OPR_CURRENCY", length = 10, nullable = false)
    private String currency;

    @Column(name = "OPR_TRANSACTION_TIME", nullable = false)
    private LocalDate transactionTime;

    /** Correspond à "BNPL Program" dans le formulaire (v-model="operation.bnplProgramId"). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PGM_ID", nullable = false)
    private Program bnplProgram;

    /**
     * Résolu par email (le frontend envoie l'email en clair, pas un customerId).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CST_ID", nullable = false)
    private Customer customer;

    @Column(name = "OPR_NB_INSTALLMENTS")
    private Integer numberOfInstallments;

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