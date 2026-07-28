package ma.s2m.nxp.fe.settings.DTO.chatbot;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatbotRequestDTO {

    @NotBlank(message = "Message is required")
    private String message;

    /** Historique de la conversation (sans le message courant), limité côté service. */
    @Builder.Default
    private List<ChatMessageDTO> history = new ArrayList<>();
}