package org.ecommersebe.ecommersebe.repositories.mongodb;

import org.ecommersebe.ecommersebe.models.mongodb.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByConversationIdOrderByTimestampAsc(String conversationId);
    List<ChatMessage> findBySenderIdOrRecipientIdOrderByTimestampDesc(String senderId, String recipientId);
}
