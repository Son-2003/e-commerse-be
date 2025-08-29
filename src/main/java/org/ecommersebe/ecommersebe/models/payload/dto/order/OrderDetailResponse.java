package org.ecommersebe.ecommersebe.models.payload.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.payload.dto.product.ProductResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
     Long id;
     int quantity;
     float unitPrice;
     float totalPrice;
     ProductResponse product;
}
