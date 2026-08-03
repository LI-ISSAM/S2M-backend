package ma.s2m.nxp.fe.settings.controllers;

import ma.s2m.nxp.fe.settings.dto.customer.KycExtractedFieldsDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import ma.s2m.nxp.fe.settings.services.ai.KycExtractionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/kyc-extraction")
public class KycExtractionController {

    private final KycExtractionService service;

    public KycExtractionController(KycExtractionService service) {
        this.service = service;
    }

    @PostMapping(value = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KycExtractedFieldsDTO> scan(@RequestParam("file") MultipartFile file)
            throws BusinessException {
        return ResponseEntity.ok(service.extract(file));
    }
}