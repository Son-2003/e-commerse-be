package org.ecommersebe.ecommersebe.models.payload.dto.size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeResponse {
    private String name;

    private int quantity;
}
