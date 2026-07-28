package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.DTO.freezinginquiry.FreezingInquiryDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class FreezingInquiriesPageResponse {

    private final List<FreezingInquiryDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}