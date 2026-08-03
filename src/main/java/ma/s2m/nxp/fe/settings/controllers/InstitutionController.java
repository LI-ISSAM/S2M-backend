package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.institution.InstitutionDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IInstitutionOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.InstitutionsPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/institutions")
public class InstitutionController {

    private final IInstitutionOrchestrationService institutionOrchestrationService;

    public InstitutionController(IInstitutionOrchestrationService institutionOrchestrationService) {
        this.institutionOrchestrationService = institutionOrchestrationService;
    }

    /**
     * Liste paginée. Compatible avec le frontend actuel qui envoie :
     * _page, _limit, name_like et lit response.headers['x-total-count'].
     */
    @GetMapping
    public ResponseEntity<Object> getAllInstitutions(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "reference_like" , required = false) String reference,
            @RequestParam(value = "tag", required = false) String tag) {

        InstitutionsPageResponse result = institutionOrchestrationService.getAllInstitutions(page, limit, name,reference, tag);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getInstitutionById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(institutionOrchestrationService.getInstitutionById(id));
    }

    @PostMapping
    public ResponseEntity<InstitutionDTO> createInstitution(@Valid @RequestBody InstitutionDTO institutionDTO)
            throws BusinessException {
        InstitutionDTO created = institutionOrchestrationService.createInstitution(institutionDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDTO> updateInstitution(@PathVariable Long id,
                                                            @Valid @RequestBody InstitutionDTO institutionDTO)
            throws BusinessException {
        return ResponseEntity.ok(institutionOrchestrationService.updateInstitution(id, institutionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Long id) throws BusinessException {
        institutionOrchestrationService.deleteInstitution(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "reference_like" , required = false) String reference,
            @RequestParam(value = "tag", required = false) String tag) {

        List<InstitutionDTO> institutions = institutionOrchestrationService.getAllInstitutionsForExport(
                name,reference,tag
        );
        List<String> headers = List.of("ID","Name","Reference","Type","Status","Email","Address");
        List<String[]> rows = institutions.stream()
                .map(i->new String[]{
                        String.valueOf(i.getId()),
                        i.getName(),
                        i.getReference(),
                        i.getType(),
                        i.getStatus(),
                        i.getContact().getEmail(),
                        i.getContact().getAddress()


                })
                .toList();
        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "institutions.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);

    }
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "reference_like" , required = false) String reference,
            @RequestParam(value = "tag", required = false) String tag) {

        List<InstitutionDTO> institutions = institutionOrchestrationService.getAllInstitutionsForExport(
                name,reference,tag
        );
        List<String> headers = List.of("ID","Name","Reference","Type","Status","Email","Address");
        List<String[]> rows = institutions.stream()
                .map(i->new String[]{
                        String.valueOf(i.getId()),
                        i.getName(),
                        i.getReference(),
                        i.getType(),
                        i.getStatus(),
                        i.getContact().getEmail(),
                        i.getContact().getAddress()


                })
                .toList();
        byte[] pdf = PdfExportUtil.toPdf("Liste des institutions",headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "pdf.csv");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);

    }
}