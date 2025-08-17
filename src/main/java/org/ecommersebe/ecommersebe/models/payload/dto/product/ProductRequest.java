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
    private String name;

    private float price;

    private boolean isBestSeller;

    private String description;

    private EntityStatus status;

    private String image;

    private Category category;

    private SubCategory subCategory;

    private List<SizeRequest> sizes;
}
