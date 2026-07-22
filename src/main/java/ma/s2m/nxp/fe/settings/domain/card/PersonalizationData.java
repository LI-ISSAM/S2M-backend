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
public class PersonalizationData {

    @Column(name = "PD_FIRST_CREATION_DATE")
    private LocalDate firstCreationDate;

    @Column(name = "PD_PREPARATION_DATE")
    private LocalDate preparationDate;

    @Column(name = "PD_PERSONALIZATION_STATUS", length = 30)
    private String personalizationStatus;

    @Column(name = "PD_LAST_PERSONALIZATION_DATE")
    private LocalDate lastPersonalizationDate;

    @Column(name = "PD_LAST_PERSONALIZATION_BATCH", length = 50)
    private String lastPersonalizationBatch;

    @Column(name = "PD_FILE_NAME", length = 200)
    private String fileName;
}