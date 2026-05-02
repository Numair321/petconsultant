package com.petconsultant.service;

import com.petconsultant.request.MessageRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.ChatResponse;
import com.petconsultant.response.MessageResponse;
import java.util.List;

public interface ChatService {
    ApiResponse startChat(Long vetId);
    List<MessageResponse> getChatHistory(Long chatId);
    ApiResponse sendMessage(MessageRequest request);
    List<ChatResponse> getConversations();
}
