package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.merchant.MerchantDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IMerchantOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.MerchantsPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final IMerchantOrchestrationService merchantOrchestrationService;

    public MerchantController(IMerchantOrchestrationService merchantOrchestrationService) {
        this.merchantOrchestrationService = merchantOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel (_page, _limit, name_like),
     * + reference_like pour la recherche par référence marchand,
     * + institutionId pour filtrer les marchands d'une institution.
     */
    @GetMapping
    public ResponseEntity<Object> getAllMerchants(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "institutionId", required = false) Long institutionId) {

        MerchantsPageResponse result =
                merchantOrchestrationService.getAllMerchants(page, limit, name, reference, institutionId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> getMerchantById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(merchantOrchestrationService.getMerchantById(id));
    }

    @PostMapping
    public ResponseEntity<MerchantDTO> createMerchant(@Valid @RequestBody MerchantDTO merchantDTO)
            throws BusinessException {
        MerchantDTO created = merchantOrchestrationService.createMerchant(merchantDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(@PathVariable Long id,
                                                      @Valid @RequestBody MerchantDTO merchantDTO)
            throws BusinessException {
        return ResponseEntity.ok(merchantOrchestrationService.updateMerchant(id, merchantDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) throws BusinessException {
        merchantOrchestrationService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "institutionId", required = false) Long institutionId) {

        List<MerchantDTO> merchants = merchantOrchestrationService.getAllMerchantsForExport(
                name, reference, institutionId
        );
        List<String> headers = List.of("ID", "Name", "Reference", "mccCode", "Institution", "Type", "Status", "Corporate Name", "Branch");
        List<String[]> rows = merchants.stream()
                .map(m -> new String[]{
                        String.valueOf(m.getId()),
                        m.getName(),
                        m.getReference(),
                        m.getMccCode(),
                        m.getInstitutionName(),
                        m.getType(),
                        m.getStatus(),
                        m.getCorporateName(),
                        m.getBranch()


                })
                .toList();
        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "merchants.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);

    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "institutionId", required = false) Long institutionId) {


        List<MerchantDTO> merchants = merchantOrchestrationService.getAllMerchantsForExport(
                name, reference, institutionId
        );
        List<String> headers = List.of("ID", "Name", "Reference", "mccCode", "Institution", "Type", "Status", "Corporate Name", "Branch");
        List<String[]> rows = merchants.stream()
                .map(m -> new String[]{
                        String.valueOf(m.getId()),
                        m.getName(),
                        m.getReference(),
                        m.getMccCode(),
                        m.getInstitutionName(),
                        m.getType(),
                        m.getStatus(),
                        m.getCorporateName(),
                        m.getBranch()


                })
                .toList();
        byte[] pdf = PdfExportUtil.toPdf("Liste des merchants", headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "pdf.csv");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);
    }
}