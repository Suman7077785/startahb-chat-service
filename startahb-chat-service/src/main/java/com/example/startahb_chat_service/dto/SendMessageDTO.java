package com.example.startahb_chat_service.dto;

import lombok.Data;

@Data
public class SendMessageDTO {
    private Long senderId;
    private Long receiverId;
    private String message;

}
