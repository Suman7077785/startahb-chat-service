package com.example.startahb_chat_service.controller;

import lombok.Data;

@Data
public class SendMessageRequest {
    private Long senderId;
    private Long receiverId;
    private String message;


}
