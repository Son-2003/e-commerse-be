package org.ecommersebe.ecommersebe.models.payload.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    Long id;
    int quantity;
    String name;
    String image;
    float price;
    String size;
}
