package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.installment_plan.InstallmentPlanDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IInstallmentPlanOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.InstallmentPlansPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/installmentPlans")
public class InstallmentPlanController {

    private final IInstallmentPlanOrchestrationService installmentPlanOrchestrationService;

    public InstallmentPlanController(IInstallmentPlanOrchestrationService installmentPlanOrchestrationService) {
        this.installmentPlanOrchestrationService = installmentPlanOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, customerName_like
     * (jointure vers Customer.fullName), offerName_like (jointure vers Offer.name).
     */
    @GetMapping
    public ResponseEntity<Object> getAllInstallmentPlans(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "customerName_like", required = false) String customerName,
            @RequestParam(value = "offerName_like", required = false) String offerName,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "offerId", required = false) Long offerId) {

        InstallmentPlansPageResponse result = installmentPlanOrchestrationService.getAllInstallmentPlans(
                page, limit, customerName, offerName, customerId, offerId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallmentPlanDTO> getInstallmentPlanById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(installmentPlanOrchestrationService.getInstallmentPlanById(id));
    }

    @PostMapping
    public ResponseEntity<InstallmentPlanDTO> createInstallmentPlan(@Valid @RequestBody InstallmentPlanDTO dto)
            throws BusinessException {
        InstallmentPlanDTO created = installmentPlanOrchestrationService.createInstallmentPlan(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstallmentPlanDTO> updateInstallmentPlan(@PathVariable Long id,
                                                                    @Valid @RequestBody InstallmentPlanDTO dto)
            throws BusinessException {
        return ResponseEntity.ok(installmentPlanOrchestrationService.updateInstallmentPlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallmentPlan(@PathVariable Long id) throws BusinessException {
        installmentPlanOrchestrationService.deleteInstallmentPlan(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "customerName_like", required = false) String customerName,
            @RequestParam(value = "offerName_like", required = false) String offerName,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "offerId", required = false) Long offerId) {

        List<InstallmentPlanDTO> installment_plan = installmentPlanOrchestrationService.getAllInstallmentPlansForExport(
                customerName,offerName,customerId,offerId
        );
        List<String> headers = List.of("ID","Customer","Offer","Total Amount","Installments","Start Date","Status");
        List<String[]> rows = installment_plan.stream()
                .map(i->new String[]{
                        String.valueOf(i.getId()),
                        i.getCustomerName(),
                        i.getOfferName(),
                        String.valueOf(i.getTotalAmount()),
                        String.valueOf(i.getNumberOfInstallments()),
                        String.valueOf(i.getStartDate()),
                        i.getStatus()


                })
                .toList();
        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "installment plans.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);

    }
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "customerName_like", required = false) String customerName,
            @RequestParam(value = "offerName_like", required = false) String offerName,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "offerId", required = false) Long offerId) {

        List<InstallmentPlanDTO> installment_plan = installmentPlanOrchestrationService.getAllInstallmentPlansForExport(
                customerName,offerName,customerId,offerId
        );
        List<String> headers = List.of("ID","Customer","Offer","Total Amount","Installments","Start Date","Status");
        List<String[]> rows = installment_plan.stream()
                .map(i->new String[]{
                        String.valueOf(i.getId()),
                        i.getCustomerName(),
                        i.getOfferName(),
                        String.valueOf(i.getTotalAmount()),
                        String.valueOf(i.getNumberOfInstallments()),
                        String.valueOf(i.getStartDate()),
                        i.getStatus()


                })
                .toList();
        byte[] pdf = PdfExportUtil.toPdf("Liste des installment plans",headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "pdf.csv");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);

    }
}