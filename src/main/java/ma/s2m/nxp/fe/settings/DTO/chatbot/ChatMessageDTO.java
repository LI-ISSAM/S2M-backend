package ma.s2m.nxp.fe.settings.DTO.chatbot;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessageDTO {
    private String role;    // "user" ou "assistant"
    private String content;
}