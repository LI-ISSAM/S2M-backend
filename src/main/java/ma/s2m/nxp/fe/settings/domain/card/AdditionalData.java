
package ma.s2m.nxp.fe.settings.domain.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AdditionalData {

    @Column(name = "AD_FIRST_CODE_SERVICE", length = 100)
    private String firstCodeService;

    @Column(name = "AD_SECOND_CODE_SERVICE", length = 100)
    private String secondCodeService;

    @Column(name = "AD_THIRD_CODE_SERVICE", length = 100)
    private String thirdCodeService;

    @Column(name = "AD_INTERNET_ORDER", length = 10)
    private String internetOrder;

    @Column(name = "AD_MAIL_ORDER", length = 10)
    private String mailOrder;

    @Column(name = "AD_CHIP_FLAG", length = 10)
    private String chipFlag;

    @Column(name = "AD_MAGNETIC_FLAG", length = 10)
    private String magneticFlag;

    @Column(name = "AD_PIN_GENERATION", length = 10)
    private String pinGeneration;

    @Column(name = "AD_LAST_TRANSACTION_DATE")
    private LocalDate lastTransactionDate;

    @Column(name = "AD_ANONYMOUS_CARD", length = 10)
    private String anonymousCard;

    @Column(name = "AD_PIN_METHOD", length = 20)
    private String pinMethod;

    @Column(name = "AD_NEW_CARD_DESIGN", length = 10)
    private String newCardDesign;
}