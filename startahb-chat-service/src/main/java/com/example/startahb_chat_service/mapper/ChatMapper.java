package com.example.startahb_chat_service.mapper;

import com.example.startahb_chat_service.dto.ChatMessageResponseDTO;
import com.example.startahb_chat_service.entity.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {
    public ChatMessageResponseDTO toDTO(ChatMessage message) {
        return ChatMessageResponseDTO.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoomId())
                .senderId(message.getSenderId())
                .message(message.getMessage())
                .status(message.getStatus().name())
                .createdAt(message.getCreatedAt().toString())
                .build();
    }
}
