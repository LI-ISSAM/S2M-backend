package ma.s2m.nxp.fe.settings.domain.program;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.Enums.Channel;
import ma.s2m.nxp.fe.settings.Enums.ProgramStatus;
import ma.s2m.nxp.fe.settings.Enums.ProgramType;
import ma.s2m.nxp.fe.settings.domain.member.Institution;

import java.util.HashSet;
import java.util.Set;


/**
 * Entité Program, rattachée à une Institution (relation ManyToOne).
 * Une institution peut avoir plusieurs programmes ; un programme
 * appartient exactement à une institution.
 */
@Entity
@Table(name = "PROGRAM", uniqueConstraints = {
        // Le nom du programme doit être unique globalement (tous instituts confondus)
        @UniqueConstraint(columnNames = {"PGM_NAME"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Program {

    public static final String PROGRAM_SEQ_NAME = "PROGRAM_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PROGRAM_SEQ_NAME)
    @SequenceGenerator(name = PROGRAM_SEQ_NAME, sequenceName = PROGRAM_SEQ_NAME, allocationSize = 1)
    @Column(name = "PGM_ID")
    @ToString.Include
    private Long id;

    @Column(name = "PGM_NAME", length = 50, nullable = false)
    @ToString.Include
    private String name;

    /**
     * Relation vers l'institution propriétaire du programme.
     * LAZY pour éviter de charger l'institution complète à chaque requête de liste.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INST_ID", nullable = false)
    private Institution institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "PGM_TYPE", length = 20, nullable = false)
    private ProgramType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "PGM_STATUS", length = 20, nullable = false)
    private ProgramStatus status;

    @Embedded
    private Eligibility eligibility;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PROGRAM_ALLOWED_SUBBINS", joinColumns = @JoinColumn(name = "PGM_ID"))
    @Column(name = "SUB_BIN", length = 30)
    @Builder.Default
    private Set<String> allowedSubBins = new HashSet<>();

    @Embedded
    private Fee fee;

    @Embedded
    private Limit limit;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PROGRAM_ALLOWED_CHANNELS", joinColumns = @JoinColumn(name = "PGM_ID"))
    @Column(name = "CHANNEL", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Channel> allowedChannels = new HashSet<>();

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