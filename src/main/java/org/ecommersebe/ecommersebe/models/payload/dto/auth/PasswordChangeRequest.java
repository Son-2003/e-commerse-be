package org.ecommersebe.ecommersebe.models.payload.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeRequest {
    @NotBlank(message = "Old password must not be blank")
    String oldPassword;
    @NotBlank(message = "New password must not be blank")
    String newPassword;
}
