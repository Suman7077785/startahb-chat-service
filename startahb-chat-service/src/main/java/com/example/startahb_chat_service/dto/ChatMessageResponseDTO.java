package com.example.startahb_chat_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponseDTO {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String message;
    private String status;
    private String createdAt;
}
