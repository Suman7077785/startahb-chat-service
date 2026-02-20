package com.example.startahb_chat_service.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class OnlineUserService {
    private final Map<Long, LocalDateTime> onlineUsers = new HashMap<>();

    public void markOnline(Long userId) {
        onlineUsers.put(userId, LocalDateTime.now());
    }

    public void markOffline(Long userId) {
        onlineUsers.remove(userId);
    }

    public Set<Long> getOnlineUsers() {
        return onlineUsers.keySet();
    }
}
