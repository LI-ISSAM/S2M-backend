package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.offer.OfferDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IOfferOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.OffersPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final IOfferOrchestrationService offerOrchestrationService;

    public OfferController(IOfferOrchestrationService offerOrchestrationService) {
        this.offerOrchestrationService = offerOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, name_like, programId,
     * réponse paginée avec header X-Total-Count.
     * Note : getDefaultOffer() côté frontend filtre côté client sur isDefault
     * après avoir appelé cet endpoint avec un grand _limit — aucune route
     * dédiée n'est donc nécessaire pour ce cas.
     */
    @GetMapping
    public ResponseEntity<Object> getAllOffers(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "programId", required = false) Long programId) {

        OffersPageResponse result = offerOrchestrationService.getAllOffers(page, limit, name, programId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(offerOrchestrationService.getOfferById(id));
    }

    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferDTO offerDTO)
            throws BusinessException {
        OfferDTO created = offerOrchestrationService.createOffer(offerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id,
                                                @Valid @RequestBody OfferDTO offerDTO)
            throws BusinessException {
        return ResponseEntity.ok(offerOrchestrationService.updateOffer(id, offerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) throws BusinessException {
        offerOrchestrationService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "programId", required = false) Long programId) {

        List<OfferDTO> offers = offerOrchestrationService.getAllOffersForExport(name,programId);

        List<String> headers = List.of("ID", "Name","Program Name","Number Of Installments","Start Date","Status","Limit");

        List<String[]> rows = offers.stream()
                .map(o -> new String[]{
                        String.valueOf(o.getId()),
                        o.getName(),
                        o.getProgramName(),
                        String.valueOf(o.getNumberOfInstallments()),
                        String.valueOf(o.getStartDate()),
                        o.getStatus(),
                        String.valueOf(o.getLimit().getMaxMonthlyInstallment())
                })
                .toList();

        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "offers.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);
    }

    /**
     * Export PDF, mêmes filtres que la liste, sans pagination.
     */
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "programId", required = false) Long programId) {

        List<OfferDTO> offers = offerOrchestrationService.getAllOffersForExport(name,programId);

        List<String> headers = List.of("ID", "Name","Program Name","Number Of Installments","Start Date","Status","Limit");

        List<String[]> rows = offers.stream()
                .map(o -> new String[]{
                        String.valueOf(o.getId()),
                        o.getName(),
                        o.getProgramName(),
                        String.valueOf(o.getNumberOfInstallments()),
                        String.valueOf(o.getStartDate()),
                        o.getStatus(),
                        String.valueOf(o.getLimit().getMaxMonthlyInstallment())
                })
                .toList();

        byte[] pdf = PdfExportUtil.toPdf("Liste des offers", headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "offers.pdf");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);
    }
}