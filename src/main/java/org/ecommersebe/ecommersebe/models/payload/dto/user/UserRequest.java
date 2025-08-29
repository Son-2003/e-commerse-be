package org.ecommersebe.ecommersebe.models.payload.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.constants.AppConstants;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.RoleType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
     Long id;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, message = "Full name must have at least 2 characters")
    String fullName;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, message = "Full name must have at least 2 characters")
    String address;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Pattern(regexp = AppConstants.EMAIL_REGEX, message = "Email is invalid!")
    String email;

    @Pattern(regexp = AppConstants.PHONE_REGEX, message = "Invalid phone number!")
    String phone;
    EntityStatus status;
    String image;
}
