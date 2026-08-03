package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.dto.customer.CustomerDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.ICustomerOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.CustomersPageResponse;
import ma.s2m.nxp.fe.settings.utils.CsvExportUtil;
import ma.s2m.nxp.fe.settings.utils.PdfExportUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final ICustomerOrchestrationService customerOrchestrationService;

    public CustomerController(ICustomerOrchestrationService customerOrchestrationService) {
        this.customerOrchestrationService = customerOrchestrationService;
    }

    /**
     * Compatible avec le frontend actuel : _page, _limit, name_like,
     * réponse paginée avec header X-Total-Count.
     */
    @GetMapping
    public ResponseEntity<Object> getAllCustomers(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "lastName_like", required = false) String lastName,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "subBin", required = false) String subBin) {

        CustomersPageResponse result = customerOrchestrationService.getAllCustomers(page, limit, lastName,email, subBin);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(customerOrchestrationService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO)
            throws BusinessException {
        CustomerDTO created = customerOrchestrationService.createCustomer(customerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,
                                                      @Valid @RequestBody CustomerDTO customerDTO)
            throws BusinessException {
        return ResponseEntity.ok(customerOrchestrationService.updateCustomer(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) throws BusinessException {
        customerOrchestrationService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "lastName_like", required = false) String lastName,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "subBin", required = false) String subBin) {

        List<CustomerDTO> customers = customerOrchestrationService.getAllCustomersForExport(
                lastName, email, subBin
        );
        List<String> headers = List.of("ID","Full Name","Bank","Branch","Vip Category","Title","Gender","Company");
        List<String[]> rows = customers.stream()
                .map(c->new String[]{
                        String.valueOf(c.getId()),
                        c.getFullName(),
                        c.getBank(),
                        c.getBranch(),
                        c.getVipCategory(),
                        c.getTitle(),
                        c.getGender(),
                        c.getCompany()
                })
                .toList();
        byte[] csv = CsvExportUtil.toCsv(headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        responseHeaders.setContentDispositionFormData("attachment", "customers.csv");

        return new ResponseEntity<>(csv, responseHeaders, HttpStatus.OK);

    }
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(value = "lastName_like", required = false) String lastName,
            @RequestParam(value = "email_like", required = false) String email,
            @RequestParam(value = "subBin", required = false) String subBin) {

        List<CustomerDTO> customers = customerOrchestrationService.getAllCustomersForExport(
                lastName, email, subBin
        );
        List<String> headers = List.of("ID","Full Name","Bank","Branch","Vip Category","Title","Gender","Company");
        List<String[]> rows = customers.stream()
                .map(c->new String[]{
                        String.valueOf(c.getId()),
                        c.getFullName(),
                        c.getBank(),
                        c.getBranch(),
                        c.getVipCategory(),
                        c.getTitle(),
                        c.getGender(),
                        c.getCompany()
                })
                .toList();
        byte[] pdf = PdfExportUtil.toPdf("Liste des customers",headers, rows);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentDispositionFormData("attachment", "pdf.csv");

        return new ResponseEntity<>(pdf, responseHeaders, HttpStatus.OK);

    }
}