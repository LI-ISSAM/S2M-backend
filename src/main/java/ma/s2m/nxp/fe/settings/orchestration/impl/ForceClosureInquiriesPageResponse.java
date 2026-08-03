package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.dto.force_closure_inquiry.ForceClosureInquiryDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class ForceClosureInquiriesPageResponse {

    private final List<ForceClosureInquiryDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}