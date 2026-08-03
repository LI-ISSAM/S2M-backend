package ma.s2m.nxp.fe.settings.services.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.s2m.nxp.fe.settings.dto.customer.KycExtractedFieldsDTO;
import ma.s2m.nxp.fe.settings.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class KycExtractionService {

    private static final Set<String> VALID_ID_TYPES = Set.of("CIN", "PASSPORT", "PERMIS");
    private static final Set<String> VALID_GENDERS = Set.of("HOMME", "FEMME");

    private final OllamaVisionClient visionClient;
    private final ObjectMapper objectMapper;

    public KycExtractionService(OllamaVisionClient visionClient, ObjectMapper objectMapper) {
        this.visionClient = visionClient;
        this.objectMapper = objectMapper;
    }

    public KycExtractedFieldsDTO extract(MultipartFile file) throws BusinessException {
        String rawJson;
        try {
            byte[] fileBytes = file.getBytes();
            System.out.println("Taille image reçue : " + fileBytes.length + " octets, mimeType=" + file.getContentType());
            rawJson = visionClient.extractFields(fileBytes, file.getContentType());
        } catch (Exception e) {
            throw new BusinessException("KYC_001", "Échec de l'extraction : " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

        // Log temporaire de diagnostic — à retirer une fois le comportement du modèle validé
        System.out.println("=== Réponse brute Ollama ===");
        System.out.println(rawJson);
        System.out.println("=============================");

        String cleaned = extractJsonBlock(rawJson);

        KycExtractedFieldsDTO dto;
        try {
            dto = objectMapper.readValue(cleaned, KycExtractedFieldsDTO.class);
        } catch (Exception e) {
            throw new BusinessException("KYC_002", "Réponse IA illisible, réessayez avec une image plus nette", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // On ne garde que des valeurs correspondant réellement aux options du formulaire ;
        // sinon on les vide plutôt que de forcer une valeur incorrecte dans un select.
        if (dto.getPrimaryIdType() != null && !VALID_ID_TYPES.contains(dto.getPrimaryIdType())) {
            dto.setPrimaryIdType(null);
        }
        if (dto.getGender() != null && !VALID_GENDERS.contains(dto.getGender())) {
            dto.setGender(null);
        }

        return dto;
    }
    private String extractJsonBlock(String rawJson) {
        String cleaned = rawJson.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();
        }

        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }
}