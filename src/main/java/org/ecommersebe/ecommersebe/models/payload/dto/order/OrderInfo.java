package org.ecommersebe.ecommersebe.models.payload.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderInfo {
    int numberOfOrder;
    float totalPrice;
}
