package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.ChatEntity;
import com.petconsultant.entity.MessageEntity;
import com.petconsultant.entity.UserEntity;
import com.petconsultant.entity.VetEntity;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.ChatRepository;
import com.petconsultant.repository.MessageRepository;
import com.petconsultant.repository.UserRepository;
import com.petconsultant.repository.VetRepository;
import com.petconsultant.request.MessageRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.ChatResponse;
import com.petconsultant.response.MessageResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository    chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository    userRepository;
    private final VetRepository     vetRepository;

    public ChatServiceImpl(ChatRepository chatRepository,
                            MessageRepository messageRepository,
                            UserRepository userRepository,
                            VetRepository vetRepository) {
        this.chatRepository    = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository    = userRepository;
        this.vetRepository     = vetRepository;
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            return userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No user found in DB. Please register first."));
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
    }

    @Override
    public ApiResponse startChat(Long vetId) {
        UserEntity user = getCurrentUser();
        vetRepository.findById(vetId)
                .orElseThrow(() -> new ResourceNotFoundException("Vet not found"));

        ChatEntity chat = chatRepository.findByUserIdAndVetId(user.getId(), vetId)
                .orElseGet(() -> {
                    ChatEntity newChat = new ChatEntity();
                    newChat.setUserId(user.getId());
                    newChat.setVetId(vetId);
                    return chatRepository.save(newChat);
                });

        ChatResponse res = new ChatResponse();
        res.setId(chat.getId());
        res.setUserId(chat.getUserId());
        res.setVetId(chat.getVetId());
        res.setCreatedAt(chat.getCreatedAt());

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Chat session started");
        response.setData(res);
        return response;
    }

    @Override
    public List<MessageResponse> getChatHistory(Long chatId) {
        List<MessageEntity> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
        List<MessageResponse> result = new ArrayList<>();
        for (MessageEntity msg : messages) {
            MessageResponse res = new MessageResponse();
            res.setId(msg.getId());
            res.setChatId(msg.getChatId());
            res.setSenderId(msg.getSenderId());
            res.setSenderType(msg.getSenderType());
            res.setMessage(msg.getMessage());
            res.setCreatedAt(msg.getCreatedAt());
            result.add(res);
        }
        return result;
    }

    @Override
    public ApiResponse sendMessage(MessageRequest request) {
        UserEntity user = getCurrentUser();

        MessageEntity msg = new MessageEntity();
        msg.setChatId(request.getChatId());
        msg.setSenderId(user.getId());
        msg.setSenderType("USER");
        msg.setMessage(request.getMessage());

        messageRepository.save(msg);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Message sent");
        response.setData(null);
        return response;
    }

    @Override
    public List<ChatResponse> getConversations() {
        UserEntity user = getCurrentUser();
        List<ChatEntity> chats = chatRepository.findByUserId(user.getId());
        List<ChatResponse> result = new ArrayList<>();
        for (ChatEntity chat : chats) {
            ChatResponse res = new ChatResponse();
            res.setId(chat.getId());
            res.setUserId(chat.getUserId());
            res.setVetId(chat.getVetId());
            res.setCreatedAt(chat.getCreatedAt());
            vetRepository.findById(chat.getVetId())
                    .ifPresent(v -> res.setVetName(v.getFullName()));
            result.add(res);
        }
        return result;
    }
}
