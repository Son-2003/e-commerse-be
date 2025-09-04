package org.ecommersebe.ecommersebe.models.payload.dto.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.payload.dto.product.ProductResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {
     Long id;
     int rating;
     int quantity;
     float totalPrice;
     String comment;
     String image;
     String createdDate;
     EntityStatus status;
     UserResponse user;
     ProductResponse product;
}
