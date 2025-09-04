package org.ecommersebe.ecommersebe.models.payload.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    Long id;

    @NotBlank(message = "Comment must not be blank!")
    @Size(min = 20, message = "Comment should be greater than 20 characters")
    String comment;

    @NotNull(message = "Rating must not be null!")
    @Range(min = 1, max = 5, message = "Rating should be between 1 to 5 star")
    int rating;

    @NotBlank(message = "Image must not be blank!")
    String image;

    @NotNull(message = "Product id must not be blank!")
    Long productId;
}
