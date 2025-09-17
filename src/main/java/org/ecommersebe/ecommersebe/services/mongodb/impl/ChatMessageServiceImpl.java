package org.ecommersebe.ecommersebe.services.mongodb.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.mongodb.ChatMessage;
import org.ecommersebe.ecommersebe.repositories.mongodb.ChatMessageRepository;
import org.ecommersebe.ecommersebe.services.mongodb.ChatMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void processMessage(ChatMessage chatMessage) {
        String conv = conversationId(chatMessage.getSenderId(), chatMessage.getRecipientId());
        chatMessage.setConversationId(conv);
        chatMessage.setTimestamp(Instant.now());

        ChatMessage saved = messageRepository.save(chatMessage);

        // broadcast to topic for this conversation
        messagingTemplate.convertAndSend("/topic/messages/" + conv, saved);
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        String conv = conversationId(senderId, recipientId);
        return messageRepository.findByConversationIdOrderByTimestampAsc(conv);
    }

    @Override
    public List<ChatMessage> getConversationsOfUser(String userId) {
        List<ChatMessage> all = messageRepository.findBySenderIdOrRecipientIdOrderByTimestampDesc(userId, userId);

        // group by conversationId and keep the first (latest) message per conversation
        Map<String, ChatMessage> map = new LinkedHashMap<>();
        for (ChatMessage m : all) {
            if (!map.containsKey(m.getConversationId())) {
                map.put(m.getConversationId(), m);
            }
        }
        return new ArrayList<>(map.values());
    }

    private String conversationId(String a, String b) {
        if (a == null || b == null) return UUID.randomUUID().toString();
        List<String> pair = new ArrayList<>(Arrays.asList(a, b));
        Collections.sort(pair);
        return pair.get(0) + "_" + pair.get(1);
    }
}
