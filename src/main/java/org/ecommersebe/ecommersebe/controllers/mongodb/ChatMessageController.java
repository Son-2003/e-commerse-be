package org.ecommersebe.ecommersebe.controllers.mongodb;

import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.mongodb.ChatMessage;
import org.ecommersebe.ecommersebe.services.mongodb.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.processMessage(chatMessage);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public List<ChatMessage> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId
    ) {
        return chatMessageService.findChatMessages(senderId, recipientId);
    }

    @GetMapping("/conversations/{userId}")
    public List<ChatMessage> getConversationsOfUser(
            @PathVariable String userId
    ) {
        return chatMessageService.getConversationsOfUser(userId);
    }
}
