package ma.s2m.nxp.fe.settings.orchestration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.s2m.nxp.fe.settings.dto.card.CardDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class CardsPageResponse {

    private final List<CardDTO> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}