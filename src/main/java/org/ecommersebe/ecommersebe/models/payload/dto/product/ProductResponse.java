package org.ecommersebe.ecommersebe.models.payload.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ecommersebe.ecommersebe.models.enums.Category;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.SubCategory;
import org.ecommersebe.ecommersebe.models.payload.dto.size.SizeResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
     Long id;

     String name;

     float price;

     boolean isBestSeller;

     String description;

     EntityStatus status;

     String image;

     Category category;

     SubCategory subCategory;

     List<SizeResponse> sizes;
}
