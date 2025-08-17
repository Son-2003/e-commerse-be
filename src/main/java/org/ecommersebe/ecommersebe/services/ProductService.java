package org.ecommersebe.ecommersebe.services;

import org.ecommersebe.ecommersebe.models.payload.dto.product.ProductResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.product.ProductRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.product.SearchProductRequest;
import org.springframework.data.domain.Page;


public interface ProductService extends BaseService<ProductRequest, ProductResponse>{
    Page<ProductResponse> getAll(int pageNo, int pageSize, String sortBy, String sortDir, SearchProductRequest request);
}
