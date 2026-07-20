package ma.s2m.nxp.fe.settings.domain.merchant;


import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.MerchantStatus;
import ma.s2m.nxp.fe.settings.Enums.MerchantType;
import ma.s2m.nxp.fe.settings.domain.member.Institution;

@Entity
@Table(name = "MERCHANT", uniqueConstraints = {
        // La référence marchand doit être unique globalement
        @UniqueConstraint(columnNames = "MER_REFERENCE")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Merchant {

    public static final String MERCHANT_SEQ_NAME = "MERCHANT_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MERCHANT_SEQ_NAME)
    @SequenceGenerator(name = MERCHANT_SEQ_NAME, sequenceName = MERCHANT_SEQ_NAME, allocationSize = 1)
    @Column(name = "MER_ID")
    @ToString.Include
    private Long id;

    @Column(name = "MER_NAME", length = 50, nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "MER_REFERENCE", length = 30, nullable = false)
    @ToString.Include
    private String reference;

    @Column(name = "MER_MCC_CODE", length = 10, nullable = false, unique = true)
    private String mccCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INST_ID", nullable = false)
    private Institution institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "MER_TYPE", length = 20, nullable = false)
    private MerchantType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "MER_STATUS", length = 20, nullable = false)
    private MerchantStatus status;

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