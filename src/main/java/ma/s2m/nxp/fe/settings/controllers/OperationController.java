package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.operation.OperationDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IOperationOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.OperationsPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Compatible avec OperationService.js : _page, _limit, reference_like
 * (recherche par référence Merchant), email_like (recherche par email
 * Customer), programName_like (recherche par nom de Program).
 */
@RestController
@RequestMapping("/api/v1/operations")
public class OperationController {

    private final IOperationOrchestrationService operationOrchestrationService;

    public OperationController(IOperationOrchestrationService operationOrchestrationService) {
        this.operationOrchestrationService = operationOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllOperations(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "programName_like", required = false) String programName) {

        OperationsPageResponse result = operationOrchestrationService.getAllOperations(page, limit, reference, email, programName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDTO> getOperationById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(operationOrchestrationService.getOperationById(id));
    }

    @PostMapping
    public ResponseEntity<OperationDTO> createOperation(@Valid @RequestBody OperationDTO dto) throws BusinessException {
        OperationDTO created = operationOrchestrationService.createOperation(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperationDTO> updateOperation(@PathVariable Long id, @Valid @RequestBody OperationDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(operationOrchestrationService.updateOperation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) throws BusinessException {
        operationOrchestrationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Export CSV, mêmes filtres que la liste, sans pagination.
     */
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "programName_like", required = false) String programName) {

        List<OperationDTO> operations = operationOrchestrationService.getAllOperationsForExport(reference, email, programName);

        List<String> headers = List.of("ID", "PAN", "Issuing Bank", "Acquiring", "RRN", "STAN",
                "Merchant Id", "Amount", "Currency", "Transaction Time", "BNPL Program Id",
                "Customer Email", "Installments");

        List<String[]> rows = operations.stream()
                .map(o -> new String[]{
                        String.valueOf(o.getId()),
                        o.getPan(),
                        o.getIssuingBank(),
                        o.getAcquiring(),
                        o.getRrn(),
                        o.getStan(),
                        String.valueOf(o.getMerchantId()),
                        String.valueOf(o.getAmount()),
                        o.getCurrency(),
                        String.valueOf(o.getTransactionTime()),
                        String.valueOf(o.getBnplProgramId()),
                        o.getCustomerEmail(),
                        String.valueOf(o.getNumberOfInstallments())
                })
                .toList();

        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "operations.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);
    }

    /**
     * Export PDF, mêmes filtres que la liste, sans pagination.
     */
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "reference_like", required = false) String reference,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "programName_like", required = false) String programName) {

        List<OperationDTO> operations = operationOrchestrationService.getAllOperationsForExport(reference, email, programName);

        List<String> headers = List.of("ID", "PAN", "Issuing Bank", "Acquiring", "RRN", "STAN",
                "Amount", "Currency", "Transaction Time", "Customer Email", "Installments");

        List<String[]> rows = operations.stream()
                .map(o -> new String[]{
                        String.valueOf(o.getId()),
                        o.getPan(),
                        o.getIssuingBank(),
                        o.getAcquiring(),
                        o.getRrn(),
                        o.getStan(),
                        String.valueOf(o.getAmount()),
                        o.getCurrency(),
                        String.valueOf(o.getTransactionTime()),
                        o.getCustomerEmail(),
                        String.valueOf(o.getNumberOfInstallments())
                })
                .toList();

        byte[] pdf = PdfExportUtil.toPdf("Liste des opérations", headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "operations.pdf");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);
    }

}