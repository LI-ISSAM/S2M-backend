package ma.s2m.nxp.fe.settings.dto.card;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PersonalizationDataDTO {
    private LocalDate firstCreationDate;
    private LocalDate preparationDate;
    private String personalizationStatus; // PERSONALIZED / NOT_PERSONALIZED
    private LocalDate lastPersonalizationDate;
    private String lastPersonalizationBatch;
    private String fileName;
}