package org.ecommersebe.ecommersebe.models.payload.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchOrderRequest {
     LocalDateTime dateFrom;
     LocalDateTime dateTo;
     List<OrderStatus> statuses;
     String searchText;
}
