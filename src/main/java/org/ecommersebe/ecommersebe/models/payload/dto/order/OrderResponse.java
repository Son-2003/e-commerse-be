package org.ecommersebe.ecommersebe.models.payload.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.OrderStatus;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.PaymentHistoryResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
     Long id;
     Long orderCode;
     LocalDateTime createdDate;
     OrderStatus status;
     boolean isFeedBack;
     float totalAmount;
     String address;
     UserResponse user;
     PaymentHistoryResponse paymentHistory;
     FeedbackResponse feedback;
     List<OrderDetailResponse> orderDetails;
}
