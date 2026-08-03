package ma.s2m.nxp.fe.settings.domain.subscription;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.OnboardingMode;
import ma.s2m.nxp.fe.settings.enums.SubscriptionStatus;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.offer.Offer;
import ma.s2m.nxp.fe.settings.domain.program.Program;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Entité Subscription (diagramme BNPL, package Global Set Up).
 * Le champ subscriptionId est un identifiant métier auto-généré (ex: SUB-483920),
 * distinct de l'id technique.
 */
@Entity
@Table(name = "SUBSCRIPTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = "SUB_SUBSCRIPTION_ID")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Subscription {

    public static final String SUBSCRIPTION_SEQ_NAME = "SUBSCRIPTION_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SUBSCRIPTION_SEQ_NAME)
    @SequenceGenerator(name = SUBSCRIPTION_SEQ_NAME, sequenceName = SUBSCRIPTION_SEQ_NAME, allocationSize = 1)
    @Column(name = "SUB_ID")
    @ToString.Include
    private Long id;

    @Column(name = "SUB_SUBSCRIPTION_ID", length = 30, updatable = false)
    @ToString.Include
    private String subscriptionId;
    @Column(name = "SUB_CUSTOMER_EMAIL", length = 100)
    private String customerEmail;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CST_ID", nullable = false,unique=true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PGM_ID", nullable = false)
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFR_ID")
    private Offer offer;

    @Column(name = "SUB_SUBSCRIPTION_DATE", nullable = false)
    private LocalDate subscriptionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "SUB_MODE", length = 20, nullable = false)
    private OnboardingMode mode;

    @Enumerated(EnumType.STRING)
    @Column(name = "SUB_STATUS", length = 20, nullable = false)
    private SubscriptionStatus status;

    @Embedded
    private SubscriptionEligibility eligibility;

    @Column(name = "CREATED_AT", updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}