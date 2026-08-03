package ma.s2m.nxp.fe.settings.domain.installment_plan;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.InstallmentPlanStatus;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.offer.Offer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "INSTALLMENT_PLAN")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class InstallmentPlan {

    public static final String INSTALLMENT_PLAN_SEQ_NAME = "INSTALLMENT_PLAN_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = INSTALLMENT_PLAN_SEQ_NAME)
    @SequenceGenerator(name = INSTALLMENT_PLAN_SEQ_NAME, sequenceName = INSTALLMENT_PLAN_SEQ_NAME, allocationSize = 1)
    @Column(name = "IPL_ID")
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CST_ID", nullable = false)
    private Customer customer;
    @Column(name = "CST_EMAIL", length = 150, nullable = false, updatable = false)
    private String customerEmail;

    /**
     * Offre d'origine, optionnelle : un plan peut être créé sans offre rattachée
     * (cf. placeholder frontend "Rechercher une offre (optionnel)...").
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFR_ID")
    private Offer offer;

    @Column(name = "IPL_TOTAL_AMOUNT", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "IPL_NB_INSTALLMENTS", nullable = false)
    private Integer numberOfInstallments;

    @Column(name = "IPL_START_DATE", nullable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "IPL_STATUS", length = 20, nullable = false)
    private InstallmentPlanStatus status;

    /**
     * Échéancier généré automatiquement côté backend à la création/mise à jour
     * (cf. InstallmentPlanService#generateSchedule), pour que le montant/nombre
     * d'échéances persisté soit fiable même si le frontend est modifié plus tard.
     */
    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("number ASC")
    @Builder.Default
    private List<Installment> installments = new ArrayList<>();

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