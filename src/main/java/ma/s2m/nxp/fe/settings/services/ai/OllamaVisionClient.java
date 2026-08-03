package ma.s2m.nxp.fe.settings.services.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class OllamaVisionClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${spring.ai.ollama.chat.model:llava:7b}")
    private String model;

    private static final String EXTRACTION_PROMPT = """
    Tu reçois l'image d'une pièce d'identité marocaine (CIN) ou d'un passeport.
    Observe attentivement le texte visible sur le document, puis remplis un objet JSON
    avec les VRAIES valeurs lues sur l'image (jamais un exemple, jamais un placeholder).

    Format de sortie strict (uniquement ce JSON, rien d'autre) :
    {
      "firstName": <prénom réel lu sur le document, ou null si illisible>,
      "middleName": <deuxième prénom réel, ou null>,
      "lastName": <nom de famille réel, ou null>,
      "birthDate": <date de naissance réelle au format 2000-01-31, ou null>,
      "birthPlace": <lieu de naissance réel, ou null>,
      "primaryIdType": <"CIN" si carte d'identité marocaine, "PASSPORT" si passeport, sinon null>,
      "primaryId": <numéro de document réel lu sur l'image, ou null>,
      "gender": <"HOMME" ou "FEMME" déduit du document, ou null>,
      "nationality": <code ISO 3166-1 alpha-2 réel, ex MA pour Maroc, ou null>,
      "confidence": <"high", "medium" ou "low" selon ta certitude réelle>
    }

    Règle absolue : n'utilise JAMAIS les mots "null", des points de suspension, ou un
    texte d'exemple à la place d'une vraie valeur lue sur l'image. Si tu ne peux
    vraiment pas lire un champ, mets la valeur JSON null (pas la chaîne "null").
    """;

    public OllamaVisionClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String extractFields(byte[] imageBytes, String mimeType) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", EXTRACTION_PROMPT,
                "images", List.of(base64Image)
        );

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(message),
                "stream", false,
                "format", "json",
                "options", Map.of("temperature", 0.3, "num_predict", 512)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        JsonNode response = restTemplate.postForObject(
                baseUrl + "/api/chat", request, JsonNode.class);

        if (response == null || !response.has("message")) {
            throw new RuntimeException("Réponse Ollama vide ou inattendue : " + response);
        }

        return response.get("message").get("content").asText();
    }
}