package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.DTO.program.ProgramDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProgramsPageResponse {

    private final List<ProgramDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}