package org.ecommersebe.ecommersebe.models.payload.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInRequest {
    @NotBlank(message = "Email cannot be blank")
    @Size(min = 8, message = "Account must have at least 8 characters")
    String emailOrPhone;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    String password;
}
