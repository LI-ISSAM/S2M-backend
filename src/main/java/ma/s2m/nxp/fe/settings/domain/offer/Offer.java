package ma.s2m.nxp.fe.settings.domain.offer;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.OfferStatus;
import ma.s2m.nxp.fe.settings.domain.program.Program;

import java.time.LocalDate;

/**
 * Entité Offer, rattachée à un Program (relation ManyToOne).
 * Une offre "par défaut" (isDefault=true) hérite conceptuellement des
 * fee/limit du programme côté frontend ; côté backend on stocke la valeur
 * effective (recopiée au moment de la création) pour garder l'historique
 * même si le programme change plus tard.
 */
@Entity
@Table(name = "OFFER"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Offer {

    public static final String OFFER_SEQ_NAME = "OFFER_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = OFFER_SEQ_NAME)
    @SequenceGenerator(name = OFFER_SEQ_NAME, sequenceName = OFFER_SEQ_NAME, allocationSize = 1)
    @Column(name = "OFR_ID")
    @ToString.Include
    private Long id;

    @Column(name = "OFR_NAME", length = 50, nullable = false , unique = true)
    @ToString.Include

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PGM_ID", nullable = false)
    private Program program;

    @Column(name = "OFR_NB_INSTALLMENTS", nullable = false)
    private Integer numberOfInstallments;

    @Column(name = "OFR_START_DATE", nullable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "OFR_STATUS", length = 20, nullable = false)
    private OfferStatus status;

    /**
     * true = cette offre est LA référence par défaut de son programme.
     * Un seul true possible par programme (contrainte applicative, cf. OfferService).
     */
    @Column(name = "OFR_IS_DEFAULT", nullable = false)
    @Builder.Default
    private boolean isDefault = false;

    @Embedded
    private OfferFee fee;

    /**
     * true = l'offre applique ses propres limites (colonnes OFR_LMT_*) plutôt
     * que celles du programme parent.
     */
    @Column(name = "OFR_HAS_CUSTOM_LIMIT", nullable = false)
    @Builder.Default
    private boolean hasCustomLimit = false;

    @Embedded
    private OfferLimit limit;

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