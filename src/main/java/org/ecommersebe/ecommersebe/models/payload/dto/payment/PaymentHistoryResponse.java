package org.ecommersebe.ecommersebe.models.payload.dto.payment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.PaymentStatus;
import org.ecommersebe.ecommersebe.models.enums.PaymentType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryResponse {
     Long id;
     float amount;
     LocalDateTime createdDate;
     PaymentStatus status;
     PaymentType type;
}
