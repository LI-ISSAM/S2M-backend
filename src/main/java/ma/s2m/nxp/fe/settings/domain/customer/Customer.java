package ma.s2m.nxp.fe.settings.domain.customer;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.SubBin;

/**
 * Entité Customer (EndCustomer dans le diagramme de classes BNPL).
 * Le champ customerId est un identifiant métier auto-généré (distinct de
 * l'id technique), utile pour l'évaluation d'éligibilité et le suivi
 * cross-système, même si le formulaire frontend actuel ne le saisit pas.
 */
@Entity
@Table(name = "CUSTOMER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CST_CUSTOMER_ID"),
        @UniqueConstraint(columnNames = "CST_EMAIL")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Customer {

    public static final String CUSTOMER_SEQ_NAME = "CUSTOMER_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CUSTOMER_SEQ_NAME)
    @SequenceGenerator(name = CUSTOMER_SEQ_NAME, sequenceName = CUSTOMER_SEQ_NAME, allocationSize = 1)
    @Column(name = "CST_ID")
    @ToString.Include
    private Long id;

    /**
     * Identifiant métier auto-généré à la création (ex: CST-000123),
     * distinct de la clé technique CST_ID.
     */
    @Column(name = "CST_CUSTOMER_ID", length = 30, updatable = false)
    @ToString.Include
    private String customerId;

    @Column(name = "CST_FULL_NAME", length = 80, nullable = false)
    @ToString.Include
    private String fullName;

    @Column(name = "CST_AGE", nullable = false)
    private Integer age;

    @Column(name = "CST_SALARY", nullable = false)
    private java.math.BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "CST_SUB_BIN", length = 20, nullable = false)
    private SubBin subBin;

    @Column(name = "CST_PHOTO")
    private String photo;

    @Embedded
    private CustomerContact contact;

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