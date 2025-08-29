package org.ecommersebe.ecommersebe.models.payload.dto.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTAuthResponse {
    String accessToken;

    String refreshToken;

    String message;

    String tokenType = "Bearer";

    public JWTAuthResponse(String accessToken, String refreshToken, String message) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
    }
}
