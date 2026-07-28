package ma.s2m.nxp.fe.settings.domain.freezinginquiry;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.domain.card.Card;

import java.math.BigDecimal;

/**
 * Une demande de gel (freezing) rattachée à une carte. Le frontend n'autorise
 * qu'une seule inquiry active par carte (filtrage côté AddFreezingInquiry.vue),
 * relation contrainte en unique côté backend également (cf. FreezingInquiryService).
 */
@Entity
@Table(name = "FREEZING_INQUIRY", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CRD_ID")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class FreezingInquiry {

    public static final String FREEZING_INQUIRY_SEQ_NAME = "FREEZING_INQUIRY_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = FREEZING_INQUIRY_SEQ_NAME)
    @SequenceGenerator(name = FREEZING_INQUIRY_SEQ_NAME, sequenceName = FREEZING_INQUIRY_SEQ_NAME, allocationSize = 1)
    @Column(name = "FZI_ID")
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CRD_ID", nullable = false)
    private Card card;

    @Column(name = "FZI_RNN", length = 30, nullable = false)
    @ToString.Include
    private String rnn;

    @Column(name = "FZI_TRANSACTION_DETAIL", length = 500, nullable = false)
    private String transactionDetail;

    @Column(name = "FZI_OUTSTANDING_AMOUNT", nullable = false)
    private BigDecimal outstandingAmount;

    @Column(name = "FZI_FREEZING_FEE", nullable = false)
    private BigDecimal freezingFee;

    @Column(name = "FZI_FREEZING_PERIOD", nullable = false)
    private Integer freezingPeriod;

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