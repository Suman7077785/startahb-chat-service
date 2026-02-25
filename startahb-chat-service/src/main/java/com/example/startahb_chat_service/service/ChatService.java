package com.example.startahb_chat_service.service;

import com.example.startahb_chat_service.dto.ChatMessageResponseDTO;
import com.example.startahb_chat_service.dto.SendMessageDTO;
import com.example.startahb_chat_service.entity.ChatMessage;
import com.example.startahb_chat_service.entity.ChatRoom;
import com.example.startahb_chat_service.enums.ChatRoomType;
import com.example.startahb_chat_service.enums.MessageStatus;
import com.example.startahb_chat_service.enums.MessageType;
import com.example.startahb_chat_service.exception.ResourceNotFoundException;
import com.example.startahb_chat_service.mapper.ChatMapper;
import com.example.startahb_chat_service.repository.ChatMessageRepository;
import com.example.startahb_chat_service.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMapper mapper;

    // SEND MESSAGE

    public ChatMessageResponseDTO sendMessage(SendMessageDTO dto) {

        ChatRoom chatRoom = findOrCreateChatRoom(dto.getSenderId(), dto.getReceiverId());

        ChatMessage message = ChatMessage.builder()
                .chatRoomId(chatRoom.getId())
                .senderId(dto.getSenderId())
                .message(dto.getMessage())
                .messageType(MessageType.TEXT)
                .status(MessageStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);

        ChatMessageResponseDTO responseDTO = mapper.toDTO(savedMessage);

        // Broadcast to receiver
        messagingTemplate.convertAndSendToUser(
                dto.getReceiverId().toString(),
                "/queue/messages",
                responseDTO
        );

        return responseDTO;
    }

    // MARK AS DELIVERED

    public void markAsDelivered(Long messageId) {

        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        message.setStatus(MessageStatus.DELIVERED);
        chatMessageRepository.save(message);

        // Notify sender that message was delivered
        messagingTemplate.convertAndSendToUser(
                message.getSenderId().toString(),
                "/queue/status",
                mapper.toDTO(message)
        );
    }

    // MARK AS READ

    public void markAsRead(Long messageId) {

        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        message.setStatus(MessageStatus.READ);
        chatMessageRepository.save(message);

        // Notify sender that message was read
        messagingTemplate.convertAndSendToUser(
                message.getSenderId().toString(),
                "/queue/status",
                mapper.toDTO(message)
        );
    }

    public List<ChatMessageResponseDTO> getMessages(Long chatRoomId) {

        List<ChatMessage> messages =
                chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);

        return messages.stream()
                .map(mapper::toDTO)
                .toList();
    }

    // FIND OR CREATE CHAT ROOM

    private ChatRoom findOrCreateChatRoom(Long user1, Long user2) {

        return chatRoomRepository
                .findByUser1IdAndUser2Id(user1, user2)
                .or(() -> chatRoomRepository.findByUser2IdAndUser1Id(user1, user2))
                .orElseGet(() ->
                        chatRoomRepository.save(
                                ChatRoom.builder()
                                        .type(ChatRoomType.DIRECT)
                                        .user1Id(user1)
                                        .user2Id(user2)
                                        .createdAt(LocalDateTime.now())
                                        .build()
                        )
                );
    }
}
