package org.ecommersebe.ecommersebe.models.payload.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.constants.AppConstants;
import org.ecommersebe.ecommersebe.models.enums.PaymentType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    List<CartItem> cartItems;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, message = "Full name must have at least 2 characters")
    String address;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, message = "Full name must have at least 2 characters")
    String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Pattern(regexp = AppConstants.EMAIL_REGEX, message = "Email is invalid!")
    String email;

    @Pattern(regexp = AppConstants.PHONE_REGEX, message = "Invalid phone number!")
    String phone;

    PaymentType type;
}
