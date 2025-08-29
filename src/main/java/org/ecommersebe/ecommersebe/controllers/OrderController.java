package org.ecommersebe.ecommersebe.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.constants.AppConstants;
import org.ecommersebe.ecommersebe.models.enums.OrderStatus;
import org.ecommersebe.ecommersebe.models.payload.dto.order.OrderRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.order.OrderResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.order.SearchOrderRequest;
import org.ecommersebe.ecommersebe.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @PostMapping("/search")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody @Nullable SearchOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.searchOrders(pageNo, pageSize, sortBy, sortDir, request, false));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/search-customer")
    public ResponseEntity<Page<OrderResponse>> getAllOrdersByCustomer(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody @Nullable SearchOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.searchOrders(pageNo, pageSize, sortBy, sortDir, request, true));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderService.add(request));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping
    public ResponseEntity<OrderResponse> updateOrder(@RequestParam Long orderId, @RequestParam OrderStatus status, @RequestParam @Nullable String note) {
        return ResponseEntity.ok(orderService.update(orderId, status, note));
    }
}
