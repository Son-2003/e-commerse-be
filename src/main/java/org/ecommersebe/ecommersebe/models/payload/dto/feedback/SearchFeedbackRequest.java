package org.ecommersebe.ecommersebe.models.payload.dto.feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchFeedbackRequest {
    Long productId;
    LocalDateTime dateFrom;
    LocalDateTime dateTo;
    List<EntityStatus> statuses;
    int rating;
    String searchText; //product name, fullname, email
}
