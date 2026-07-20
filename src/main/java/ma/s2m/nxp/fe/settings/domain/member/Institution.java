package ma.s2m.nxp.fe.settings.domain.member;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.InstitutionStatus;
import ma.s2m.nxp.fe.settings.Enums.InstitutionType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant une institution (Bank, Issuer, Fintech Partner...).
 * Correspond exactement aux champs exposés par le formulaire frontend
 * (General / Contact / Metadata).
 */
@Entity
@Table(name = "INSTITUTION", uniqueConstraints = {
        @UniqueConstraint(columnNames = "INST_REFERENCE")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Institution {

    public static final String INSTITUTION_SEQ_NAME = "INSTITUTION_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = INSTITUTION_SEQ_NAME)
    @SequenceGenerator(name = INSTITUTION_SEQ_NAME, sequenceName = INSTITUTION_SEQ_NAME, allocationSize = 1)
    @Column(name = "INST_ID")
    @ToString.Include
    private Long id;

    // ---------- GENERAL ----------

    @Column(name = "INST_NAME", length = 50, nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "INST_REFERENCE", length = 20, nullable = false)
    @ToString.Include
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "INST_TYPE", length = 20, nullable = false)
    private InstitutionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "INST_STATUS", length = 20, nullable = false)
    private InstitutionStatus status;


    @Column(name = "INST_LOGO")
    private String logo;

    // ---------- CONTACT ----------

    @Embedded
    private Contact contact;

    // ---------- METADATA ----------

    @Column(name = "INST_DESCRIPTION", length = 500)
    private String description;

    @Column(name = "INST_ONBOARDING_DATE")
    private LocalDate onboardingDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INSTITUTION_TAGS", joinColumns = @JoinColumn(name = "INST_ID"))
    @Column(name = "TAG", length = 30)
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    // ---------- AUDIT (simple, sans dépendance externe) ----------

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