package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.program.ProgramDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.IProgramOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.ProgramsPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programs")
public class ProgramController {

    private final IProgramOrchestrationService programOrchestrationService;

    public ProgramController(IProgramOrchestrationService programOrchestrationService) {
        this.programOrchestrationService = programOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, name_like,
     * réponse paginée avec header X-Total-Count.
     */
    @GetMapping
    public ResponseEntity<Object> getAllPrograms(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "institutionId", required = false) Long institutionId) {

        ProgramsPageResponse result = programOrchestrationService.getAllPrograms(page, limit, name, institutionId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDTO> getProgramById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(programOrchestrationService.getProgramById(id));
    }

    @PostMapping
    public ResponseEntity<ProgramDTO> createProgram(@Valid @RequestBody ProgramDTO programDTO)
            throws BusinessException {
        ProgramDTO created = programOrchestrationService.createProgram(programDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramDTO> updateProgram(@PathVariable Long id,
                                                    @Valid @RequestBody ProgramDTO programDTO)
            throws BusinessException {
        return ResponseEntity.ok(programOrchestrationService.updateProgram(id, programDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) throws BusinessException {
        programOrchestrationService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "institutionId", required = false) Long institutionId) {


        List<ProgramDTO> programs = programOrchestrationService.getAllProgramsForExport(name,institutionId);

        List<String> headers = List.of("ID","Name","Institution","Type","Status","Min Age","Max Age","AllowedSubBins","Fee","Limit (Mcc Code)");

        List<String[]> rows = programs.stream()
                .map(p -> new String[]{
                        String.valueOf(p.getId()),
                        p.getName(),
                        p.getInstitutionName(),
                        p.getType(),
                        p.getStatus(),
                        String.valueOf(p.getEligibility().getMinAge()),
                        String.valueOf(p.getEligibility().getMaxAge()),
                        p.getEligibility().getAllowedSubBins().toString(),
                        String.valueOf(p.getFee().getAmount()),
                        p.getLimit().getMccCode()
                })
                .toList();

        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "programs.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);
    }

    /**
     * Export PDF, mêmes filtres que la liste, sans pagination.
     */
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "name_like", required = false) String name,
            @RequestParam(value = "institutionId", required = false) Long institutionId) {


        List<ProgramDTO> programs = programOrchestrationService.getAllProgramsForExport(name,institutionId);

        List<String> headers = List.of("ID","Name","Institution","Type","Status","Min Age","Max Age","AllowedSubBins","Fee","Limit (Mcc Code)");

        List<String[]> rows = programs.stream()
                .map(p -> new String[]{
                        String.valueOf(p.getId()),
                        p.getName(),
                        p.getInstitutionName(),
                        p.getType(),
                        p.getStatus(),
                        String.valueOf(p.getEligibility().getMinAge()),
                        String.valueOf(p.getEligibility().getMaxAge()),
                        p.getEligibility().getAllowedSubBins().toString(),
                        String.valueOf(p.getFee().getAmount()),
                        p.getLimit().getMccCode()
                })
                .toList();

        byte[] pdf = PdfExportUtil.toPdf("Liste des programs", headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "programs.pdf");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);
    }
}