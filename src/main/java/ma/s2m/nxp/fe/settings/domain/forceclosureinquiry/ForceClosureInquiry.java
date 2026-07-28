package ma.s2m.nxp.fe.settings.domain.forceclosureinquiry;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.domain.card.Card;

import java.math.BigDecimal;

/**
 * Demande de clôture forcée rattachée à une carte. Une seule inquiry active
 * par carte (filtrage frontend + contrainte unique backend), même pattern
 * que FreezingInquiry.
 */
@Entity
@Table(name = "FORCE_CLOSURE_INQUIRY", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CRD_ID")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class ForceClosureInquiry {

    public static final String FORCE_CLOSURE_INQUIRY_SEQ_NAME = "FORCE_CLOSURE_INQUIRY_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FORCE_CLOSURE_INQUIRY_SEQ_NAME)
    @SequenceGenerator(name = FORCE_CLOSURE_INQUIRY_SEQ_NAME, sequenceName = FORCE_CLOSURE_INQUIRY_SEQ_NAME, allocationSize = 1)
    @Column(name = "FCI_ID")
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CRD_ID", nullable = false)
    private Card card;

    @Column(name = "FCI_RNN", length = 30, nullable = false)
    @ToString.Include
    private String rnn;

    @Column(name = "FCI_OUTSTANDING_AMOUNT", nullable = false)
    private BigDecimal outstandingAmount;

    @Column(name = "FCI_FORCE_CLOSURE_FEE", nullable = false)
    private BigDecimal forceClosureFee;

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