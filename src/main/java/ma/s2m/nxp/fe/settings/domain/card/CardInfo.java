package ma.s2m.nxp.fe.settings.domain.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

/**
 * Champs complémentaires du tab "Card Info" (au-delà de type/status/expiryDate
 * qui restent au niveau racine de Card).
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CardInfo {

    @Column(name = "CI_STATUS_DATE")
    private LocalDate statusDate;

    @Column(name = "CI_PRIMARY_SECONDARY", length = 20)
    private String primarySecondary;

    @Column(name = "CI_PRIMARY_CARD", length = 19)
    private String primaryCard;

    @Column(name = "CI_START_DATE")
    private LocalDate startDate;

    @Column(name = "CI_LIFE_CYCLE_YEARS")
    private Integer lifeCycleYears;

    @Column(name = "CI_PIN_TRY_LIMIT")
    private Integer pinTryLimit;

    @Column(name = "CI_PIN_TRY_COUNT")
    private Integer pinTryCount;

    @Column(name = "CI_OPPOSITION_STATUS", length = 100)
    private String oppositionStatus;

    @Column(name = "CI_REASON", length = 200)
    private String reason;
}