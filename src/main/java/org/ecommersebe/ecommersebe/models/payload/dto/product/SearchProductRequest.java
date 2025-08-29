package org.ecommersebe.ecommersebe.models.payload.dto.product;

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
public class SearchProductRequest {
     String name;

     List<EntityStatus> statuses;

     List<SubCategory> subCategories;

     List<Category> categories;
}
