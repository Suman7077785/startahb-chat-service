package com.example.startahb_chat_service.controller;

import com.example.startahb_chat_service.dto.ChatMessageResponseDTO;
import com.example.startahb_chat_service.dto.SendMessageDTO;
import com.example.startahb_chat_service.service.ChatService;
import com.example.startahb_chat_service.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final OnlineUserService onlineUserService;

    @PostMapping("/send")
    public ChatMessageResponseDTO send(@RequestBody SendMessageDTO dto) {
        return chatService.sendMessage(dto);
    }

    @PutMapping("/delivered/{id}")
    public String markDelivered(@PathVariable Long id) {
        chatService.markAsDelivered(id);
        return "Message marked as DELIVERED";
    }

    @PutMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {
        chatService.markAsRead(id);
        return "Message marked as READ";
    }

    @GetMapping("/{chatRoomId}")
    public List<ChatMessageResponseDTO> getMessages(@PathVariable Long chatRoomId) {
        return chatService.getMessages(chatRoomId);
    }


    @GetMapping("/online-users")
    public Set<Long> onlineUsers() {
        return onlineUserService.getOnlineUsers();
    }
}


