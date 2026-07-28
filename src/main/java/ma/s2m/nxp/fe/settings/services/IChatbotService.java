package ma.s2m.nxp.fe.settings.services;

import ma.s2m.nxp.fe.settings.DTO.chatbot.ChatbotRequestDTO;
import ma.s2m.nxp.fe.settings.DTO.chatbot.ChatbotResponseDTO;

public interface IChatbotService {

    ChatbotResponseDTO chat(ChatbotRequestDTO request);
}