package org.ecommersebe.ecommersebe.services.mongodb;

import org.ecommersebe.ecommersebe.models.mongodb.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    void processMessage(ChatMessage chatMessage);
    List<ChatMessage> findChatMessages(String senderId, String recipientId);
    List<ChatMessage> getConversationsOfUser(String userId);
}
