package org.ecommersebe.ecommersebe.models.payload.dto.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackSummaryResponse {
    double averageRating;
    long totalFeedback;

    long oneStarCount;
    long twoStarCount;
    long threeStarCount;
    long fourStarCount;
    long fiveStarCount;
}
