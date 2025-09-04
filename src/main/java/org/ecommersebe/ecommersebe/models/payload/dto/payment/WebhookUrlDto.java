package org.ecommersebe.ecommersebe.models.payload.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebhookUrlDto {
    @NotBlank(message = "Webhook Url cannot be blank")
    String webhookUrl;
}
