package ma.s2m.nxp.fe.settings.domain.card;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.CardStatus;
import ma.s2m.nxp.fe.settings.Enums.CardType;
import ma.s2m.nxp.fe.settings.domain.customer.Customer;
import ma.s2m.nxp.fe.settings.domain.program.Program;

@Entity
@Table(name = "CARD", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CRD_CARD_NUMBER")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Card {

    public static final String CARD_SEQ_NAME = "CARD_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CARD_SEQ_NAME)
    @SequenceGenerator(name = CARD_SEQ_NAME, sequenceName = CARD_SEQ_NAME, allocationSize = 1)
    @Column(name = "CRD_ID")
    @ToString.Include
    private Long id;

    @Column(name = "CRD_CARD_NUMBER", length = 19, nullable = false)
    @ToString.Include
    private String cardNumber;

    @Column(name = "CRD_NAME_ON_CARD", length = 80, nullable = false)
    private String nameOnCard;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CST_ID", nullable = false)
    private Customer customer;

    /**
     * Programme d'origine, optionnel (cf. placeholder frontend
     * "Rechercher un programme (optionnel)...").
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PGM_ID")
    private Program program;

    @Enumerated(EnumType.STRING)
    @Column(name = "CRD_TYPE", length = 20, nullable = false)
    private CardType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "CRD_STATUS", length = 20, nullable = false)
    private CardStatus status;

    /**
     * Format MM/YY tel que saisi par le frontend (pas une date complète,
     * juste mois/année d'expiration, sans jour).
     */
    @Column(name = "CRD_EXPIRY_DATE", length = 5, nullable = false)
    private String expiryDate;

    @Column(name = "CRD_BRANCH", length = 100)
    private String branch;

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