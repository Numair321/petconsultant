package com.petconsultant.controller;

import com.petconsultant.request.MessageRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.ChatResponse;
import com.petconsultant.response.MessageResponse;
import com.petconsultant.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // POST /test/v1/api/chat/start/{vetId}
    @PostMapping("/start/{vetId}")
    public ResponseEntity<ApiResponse> startChat(@PathVariable Long vetId) {
        ApiResponse response = chatService.startChat(vetId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/chat/history/{chatId}
    @GetMapping("/history/{chatId}")
    public ResponseEntity<ApiResponse> getChatHistory(@PathVariable Long chatId) {
        List<MessageResponse> messages = chatService.getChatHistory(chatId);
        ApiResponse response = new ApiResponse("SUCCESS", "Chat history fetched", messages);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /test/v1/api/chat/send
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        ApiResponse response = chatService.sendMessage(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /test/v1/api/chat/conversations
    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse> getConversations() {
        List<ChatResponse> conversations = chatService.getConversations();
        ApiResponse response = new ApiResponse("SUCCESS", "Conversations fetched", conversations);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
