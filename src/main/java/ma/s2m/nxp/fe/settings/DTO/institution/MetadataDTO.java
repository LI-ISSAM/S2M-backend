package ma.s2m.nxp.fe.settings.DTO.institution;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MetadataDTO {

    private String description;

    private LocalDate onboardingDate;

    @Builder.Default
    private Set<String> tags = new HashSet<>();
}