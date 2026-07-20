package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.subscription.SubscriptionDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.orchestration.ISubscriptionOrchestrationService;
import ma.s2m.nxp.fe.settings.orchestration.impl.SubscriptionsPageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final ISubscriptionOrchestrationService subscriptionOrchestrationService;

    public SubscriptionController(ISubscriptionOrchestrationService subscriptionOrchestrationService) {
        this.subscriptionOrchestrationService = subscriptionOrchestrationService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSubscriptions(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_limit", defaultValue = "4") int limit,
            @RequestParam(value = "email_like", required = false) String customerEmail) {

        SubscriptionsPageResponse result = subscriptionOrchestrationService.getAllSubscriptions(page, limit, customerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(subscriptionOrchestrationService.getSubscriptionById(id));
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO)
            throws BusinessException {
        SubscriptionDTO created = subscriptionOrchestrationService.createSubscription(subscriptionDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable Long id,
                                                              @Valid @RequestBody SubscriptionDTO subscriptionDTO)
            throws BusinessException {
        return ResponseEntity.ok(subscriptionOrchestrationService.updateSubscription(id, subscriptionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) throws BusinessException {
        subscriptionOrchestrationService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}