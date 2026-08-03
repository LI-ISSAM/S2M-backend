package ma.s2m.nxp.fe.settings.dto.customer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class KycExtractedFieldsDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthDate;
    private String birthPlace;
    private String primaryIdType;
    private String primaryId;
    private String gender;
    private String nationality;
    private String confidence;
}