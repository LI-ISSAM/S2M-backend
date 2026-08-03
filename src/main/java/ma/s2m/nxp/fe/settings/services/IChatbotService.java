package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.dto.chatbot.ChatbotRequestDTO;
import ma.s2m.nxp.fe.settings.dto.chatbot.ChatbotResponseDTO;

public interface IChatbotService {

    ChatbotResponseDTO chat(ChatbotRequestDTO request);
}