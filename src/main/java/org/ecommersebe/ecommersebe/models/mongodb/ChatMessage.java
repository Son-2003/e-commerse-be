package org.ecommersebe.ecommersebe.models.mongodb;

import lombok.*;
import org.ecommersebe.ecommersebe.models.enums.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_message")
public class ChatMessage {
    @Id
    private String id;
    private String conversationId;
    private String senderId;
    private String senderName;
    private String senderAvatar;
    private String recipientId;
    private String content;
    private MessageType type;
    private Instant timestamp;
}
