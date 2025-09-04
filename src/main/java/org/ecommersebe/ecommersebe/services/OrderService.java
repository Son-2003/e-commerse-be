package org.ecommersebe.ecommersebe.services;

import org.ecommersebe.ecommersebe.models.enums.OrderStatus;
import org.ecommersebe.ecommersebe.models.payload.dto.order.OrderInfo;
import org.ecommersebe.ecommersebe.models.payload.dto.order.OrderRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.order.OrderResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.order.SearchOrderRequest;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse get(Long orderId);
    OrderResponse update(Long orderId, OrderStatus status, String note);
    OrderResponse add(OrderRequest request);
    Page<OrderResponse> searchOrders(int pageNo, int pageSize, String sortBy, String sortDir, SearchOrderRequest request, boolean isCustomer);
    OrderInfo getOrderInfo();
}
