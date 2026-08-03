package ma.s2m.nxp.fe.settings.domain.installment_plan;

import jakarta.persistence.*;
import lombok.*;
import ma.s2m.nxp.fe.settings.enums.InstallmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "INSTALLMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Installment {

    public static final String INSTALLMENT_SEQ_NAME = "INSTALLMENT_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = INSTALLMENT_SEQ_NAME)
    @SequenceGenerator(name = INSTALLMENT_SEQ_NAME, sequenceName = INSTALLMENT_SEQ_NAME, allocationSize = 1)
    @Column(name = "IST_ID")
    @ToString.Include
    private Long id;

    /**
     * Numéro d'ordre de l'échéance dans le plan (1, 2, 3...).
     */
    @Column(name = "IST_NUMBER", nullable = false)
    @ToString.Include
    private Integer number;

    @Column(name = "IST_DUE_DATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "IST_AMOUNT", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "IST_STATUS", length = 20, nullable = false)
    @Builder.Default
    private InstallmentStatus status = InstallmentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IPL_ID", nullable = false)
    private InstallmentPlan installmentPlan;
}