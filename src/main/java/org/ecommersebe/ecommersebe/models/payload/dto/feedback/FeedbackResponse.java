package org.ecommersebe.ecommersebe.models.payload.dto.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {
     Long id;
     int rating;
     String comments;
     String image;
}
