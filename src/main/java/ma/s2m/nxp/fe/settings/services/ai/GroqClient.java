package ma.s2m.nxp.fe.settings.services.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Client bas niveau, générique, pour appeler l'API Groq (compatible OpenAI
 * chat/completions). Toutes les fonctionnalités IA du projet (recommandation,
 * explication de risque, chatbot) passent par ce client unique.
 *
 * IMPORTANT : la réponse Groq/OpenAI n'a PAS la même forme que la réponse
 * Anthropic. Format Groq :
 *   { "choices": [ { "message": { "role": "assistant", "content": "..." } } ] }
 * (pas de tableau "content" avec des blocs {type, text} comme chez Anthropic).
 */
@Slf4j
@Component
public class GroqClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.api.model:llama-3.3-70b-versatile}")
    private String model;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    public GroqClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Complétion simple à partir d'un unique prompt utilisateur (pas d'historique,
     * pas de rôle système). Utilisé par ClaudeExplanationClient / ClaudeRiskExplanationClient.
     */
    public String complete(String prompt, int maxTokens) {
        return completeChat(List.of(Map.of("role", "user", "content", prompt)), maxTokens);
    }

    /**
     * Complétion avec une liste de messages complète (system / user / assistant),
     * nécessaire pour un chatbot multi-tours avec historique de conversation.
     */
    public String completeChat(List<Map<String, String>> messages, int maxTokens) {
        if (!isConfigured()) {
            log.warn("GROQ_API_KEY non configurée : appel IA ignoré.");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(
                    "model", model,
                    "max_tokens", maxTokens,
                    "messages", messages
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            return extractText(response.getBody());
        } catch (Exception e) {
            log.error("Échec de l'appel à l'API Groq : {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parse une réponse Groq/OpenAI chat/completions :
     *   choices[0].message.content  (une simple chaîne, pas un tableau de blocs)
     */
    private String extractText(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode choices = root.get("choices");
            if (choices == null || !choices.isArray() || choices.isEmpty()) {
                log.warn("Réponse Groq inattendue (pas de 'choices') : {}", responseBody);
                return null;
            }

            String content = choices.get(0).path("message").path("content").asText(null);
            return (content != null && !content.isBlank()) ? content.trim() : null;
        } catch (Exception e) {
            log.error("Impossible de parser la réponse Groq : {}", e.getMessage());
            return null;
        }
    }
}