package ma.s2m.nxp.fe.settings.domain.installment;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.CustomerInstallmentStatus;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entité de suivi MANUEL d'échéances, rattachée directement à un Customer.
 * Distincte de ma.s2m.nxp.fe.settings.domain.installmentplan.Installment,
 * qui est l'échéancier auto-généré par un InstallmentPlan.
 * Le frontend permet de créer ces lignes indépendamment (avec juste
 * suggestion/auto-complétion depuis un plan existant si détecté).
 */
@Entity
@Table(name = "CUSTOMER_INSTALLMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class CustomerInstallment {

    public static final String CUSTOMER_INSTALLMENT_SEQ_NAME = "CUSTOMER_INSTALLMENT_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CUSTOMER_INSTALLMENT_SEQ_NAME)
    @SequenceGenerator(name = CUSTOMER_INSTALLMENT_SEQ_NAME, sequenceName = CUSTOMER_INSTALLMENT_SEQ_NAME, allocationSize = 1)
    @Column(name = "CIT_ID")
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CST_ID", nullable = false)
    private Customer customer;

    @Column(name = "CIT_DUE_DATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "CIT_AMOUNT", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "CIT_STATUS", length = 20, nullable = false)
    private CustomerInstallmentStatus status;

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