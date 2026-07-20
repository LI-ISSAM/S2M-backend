package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.DTO.institution.InstitutionDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class InstitutionsPageResponse {

    private final List<InstitutionDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}