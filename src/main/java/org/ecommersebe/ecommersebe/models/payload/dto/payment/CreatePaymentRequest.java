package org.ecommersebe.ecommersebe.models.payload.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.payload.dto.order.CartItem;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentRequest {
    List<CartItem> cartItems;

    private Long orderCode;

    @NotBlank(message = "Return Url cannot be blank!")
    private String returnUrl;

    @NotBlank(message = "Cancel Url cannot be blank!")
    private String cancelUrl;
}
