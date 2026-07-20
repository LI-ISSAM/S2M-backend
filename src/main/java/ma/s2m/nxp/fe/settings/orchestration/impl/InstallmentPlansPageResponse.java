package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.DTO.installmentplan.InstallmentPlanDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class InstallmentPlansPageResponse {

    private final List<InstallmentPlanDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}