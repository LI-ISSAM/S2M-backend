package ma.s2m.nxp.fe.settings.domain.rescheduleinquiry;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.domain.card.Card;

import java.math.BigDecimal;

/**
 * Demande de rééchelonnement rattachée à une carte. Une seule inquiry active
 * par carte (filtrage frontend + contrainte unique backend), même pattern
 * que FreezingInquiry / ForceClosureInquiry.
 */
@Entity
@Table(name = "RESCHEDULE_INQUIRY", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CRD_ID")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class RescheduleInquiry {

    public static final String RESCHEDULE_INQUIRY_SEQ_NAME = "RESCHEDULE_INQUIRY_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = RESCHEDULE_INQUIRY_SEQ_NAME)
    @SequenceGenerator(name = RESCHEDULE_INQUIRY_SEQ_NAME, sequenceName = RESCHEDULE_INQUIRY_SEQ_NAME, allocationSize = 1)
    @Column(name = "RSI_ID")
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CRD_ID", nullable = false)
    private Card card;

    @Column(name = "RSI_RNN", length = 30, nullable = false)
    @ToString.Include
    private String rnn;

    @Column(name = "RSI_TRANSACTION_DETAIL", length = 500, nullable = false)
    private String transactionDetail;

    @Column(name = "RSI_RESCHEDULE_FEE", nullable = false)
    private BigDecimal rescheduleFee;

    @Column(name = "RSI_OUTSTANDING_AMOUNT", nullable = false)
    private BigDecimal outstandingAmount;

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