package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.customer.CustomerDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.ICustomerOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.CustomersPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}