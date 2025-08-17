package org.ecommersebe.ecommersebe.services.impl;

import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.entities.Product;
import org.ecommersebe.ecommersebe.models.payload.dto.product.ProductResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.product.ProductRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.product.SearchProductRequest;
import org.ecommersebe.ecommersebe.repositories.ProductRepository;
import org.ecommersebe.ecommersebe.services.ProductService;
import org.ecommersebe.ecommersebe.utils.GenericSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ProductServiceImpl extends BaseServiceImpl<Product, ProductRequest, ProductResponse> implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        super(productRepository, modelMapper, Product.class, ProductRequest.class, ProductResponse.class);
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


//    @Override
//    public FoodCategoryResponse update(FoodCategoryRequest foodCategoryRequest) {
//        FoodCategory foodCategory = foodCategoryRepository.findById(foodCategoryRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("FoodCategory", "id", foodCategoryRequest.getId()));
//        if (foodCategory != null) {
//            FoodCategory updatedFoodCategory = mapper.map(foodCategoryRequest, FoodCategory.class);
//            return mapper.map(foodCategoryRepository.save(updatedFoodCategory), FoodCategoryResponse.class);
//        }
//        return null;
//    }

    @Override
    public Page<ProductResponse> getAll(int pageNo, int pageSize, String sortBy, String sortDir, SearchProductRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Product> specification = specification(toSearchParams(request));

        Page<Product> pageResult = productRepository.findAll(specification, pageable);
        return pageResult.map(entity -> modelMapper.map(entity, ProductResponse.class));
    }

    private Specification<Product> specification(Map<String, Object> searchParams) {
        List<Specification<Product>> specs = new ArrayList<>();

        searchParams.forEach((key, value) -> {
            switch (key) {
                case "category", "subCategory", "status" ->
                        specs.add(GenericSpecification.fieldIn(key, (Collection<?>) value));

                case "searchText" -> {
                    String text = (String) value;
                    Specification<Product> searchSpec =
                            GenericSpecification.fieldContains("name", text);

                    specs.add(searchSpec);
                }
            }
        });

        Specification<Product> result = null;
        for (Specification<Product> spec : specs) {
            result = (result == null) ? spec : result.and(spec);
        }

        return result;
    }

    private Map<String, Object> toSearchParams(SearchProductRequest request) {
        Map<String, Object> params = new HashMap<>();

        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            params.put("category", request.getCategories());
        }

        if (request.getSubCategories() != null && !request.getSubCategories().isEmpty()) {
            params.put("subCategory", request.getSubCategories());
        }

        if (request.getStatuses() != null && !request.getStatuses().isEmpty()) {
            params.put("status", request.getStatuses());
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            params.put("searchText", request.getName().trim());
        }

        return params;
    }



}
