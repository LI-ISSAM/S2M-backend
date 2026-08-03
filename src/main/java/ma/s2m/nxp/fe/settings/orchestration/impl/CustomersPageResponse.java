package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.dto.customer.CustomerDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomersPageResponse {

    private final List<CustomerDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}