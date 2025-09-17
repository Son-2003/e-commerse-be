package org.ecommersebe.ecommersebe.models.mongodb;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_preview")
public class UserPreview {
    @Id
    private String id;
    private String userId;
    private String displayName;
    private String avatar;
    private boolean connected;
}
