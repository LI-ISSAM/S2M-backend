package ma.s2m.nxp.fe.settings.services.impl;

import ma.s2m.nxp.fe.settings.dto.chatbot.ChatMessageDTO;
import ma.s2m.nxp.fe.settings.dto.chatbot.ChatbotRequestDTO;
import ma.s2m.nxp.fe.settings.dto.chatbot.ChatbotResponseDTO;
import ma.s2m.nxp.fe.settings.services.IChatbotService;
import ma.s2m.nxp.fe.settings.services.ai.GroqClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Chatbot d'assistance : répond aux questions générales sur l'état du
 * système (via SystemSnapshotService), aide à résumer des situations, et
 * peut aider à rédiger des messages de communication client (ex: rappel de
 * paiement) sur simple demande dans la conversation — pas de code dédié
 * pour ça, c'est porté par les instructions du prompt système ci-dessous.
 */
@Service
public class ChatbotServiceImpl implements IChatbotService {

    private static final int MAX_TOKENS = 600;
    private static final int MAX_HISTORY_MESSAGES = 10; // évite un prompt qui grossit indéfiniment

    private final GroqClient anthropicClient;
    private final SystemSnapshotService systemSnapshotService;

    public ChatbotServiceImpl(GroqClient anthropicClient, SystemSnapshotService systemSnapshotService) {
        this.anthropicClient = anthropicClient;
        this.systemSnapshotService = systemSnapshotService;
    }

    @Override
    public ChatbotResponseDTO chat(ChatbotRequestDTO request) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", buildSystemPrompt()));

        List<ChatMessageDTO> history = request.getHistory() != null ? request.getHistory() : List.of();
        int from = Math.max(0, history.size() - MAX_HISTORY_MESSAGES);
        for (ChatMessageDTO msg : history.subList(from, history.size())) {
            String role = "assistant".equalsIgnoreCase(msg.getRole()) ? "assistant" : "user";
            messages.add(Map.of("role", role, "content", msg.getContent()));
        }

        messages.add(Map.of("role", "user", "content", request.getMessage()));

        String reply = anthropicClient.completeChat(messages, MAX_TOKENS);
        if (reply == null) {
            reply = "Désolé, je ne suis pas disponible pour le moment (vérifiez la configuration de l'IA côté serveur).";
        }

        return ChatbotResponseDTO.builder().reply(reply).build();
    }

    private String buildSystemPrompt() {
        return "Tu es l'assistant intégré de la plateforme BNPL S2M-INIT (gestion d'institutions, "
                + "programmes, offres, clients, marchands, cartes et plans de mensualités). "
                + "Tu aides les agents internes de trois façons :\n"
                + "1. Répondre aux questions générales sur l'état du système, en te basant "
                + "UNIQUEMENT sur l'instantané de données ci-dessous (ne jamais inventer de chiffres).\n"
                + "2. Résumer une situation ou un sujet qu'on te décrit, de façon claire et concise.\n"
                + "3. Aider à rédiger des messages de communication client (ex: rappel de paiement, "
                + "notification de retard) quand on te le demande — reste professionnel et courtois, "
                + "et si des informations précises manquent (nom du client, montant...), demande-les "
                + "plutôt que de les inventer.\n"
                + "Réponds toujours en français, de façon concise (quelques phrases, sauf si on "
                + "te demande explicitement un texte plus long comme un message à rédiger).\n\n"
                + systemSnapshotService.buildSnapshot();
    }
}