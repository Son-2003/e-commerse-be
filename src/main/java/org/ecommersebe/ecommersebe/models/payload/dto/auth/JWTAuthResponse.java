package org.ecommersebe.ecommersebe.models.payload.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.Category;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.SubCategory;
import org.ecommersebe.ecommersebe.models.payload.dto.size.SizeRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTAuthResponse {
    private String accessToken;

    private String refreshToken;

    private String message;

    private String tokenType = "Bearer";

    public JWTAuthResponse(String accessToken, String refreshToken, String message) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
    }
}
