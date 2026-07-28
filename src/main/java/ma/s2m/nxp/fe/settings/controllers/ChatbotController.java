package ma.s2m.nxp.fe.settings.controllers;

import jakarta.validation.Valid;
import ma.s2m.nxp.fe.settings.DTO.chatbot.ChatbotRequestDTO;
import ma.s2m.nxp.fe.settings.DTO.chatbot.ChatbotResponseDTO;
import ma.s2m.nxp.fe.settings.services.IChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatbotController {

    private final IChatbotService chatbotService;

    public ChatbotController(IChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatbotResponseDTO> ask(@Valid @RequestBody ChatbotRequestDTO request) {
        return ResponseEntity.ok(chatbotService.chat(request));
    }
}