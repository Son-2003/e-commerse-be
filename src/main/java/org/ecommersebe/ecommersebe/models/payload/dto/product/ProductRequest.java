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
public class ProductRequest {
     Long id;

     String name;

     float price;

     boolean isBestSeller;

     String description;

     EntityStatus status;

     String image;

     Category category;

     SubCategory subCategory;

     List<SizeRequest> sizes;
}
