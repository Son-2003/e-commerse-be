package org.ecommersebe.ecommersebe.models.payload.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.RoleType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     Long id;
     String fullName;
     String email;
     String address;
     String phone;
     EntityStatus status;
     RoleType role;
     String image;
}
