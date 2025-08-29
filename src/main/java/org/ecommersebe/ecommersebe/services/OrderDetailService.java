package org.ecommersebe.ecommersebe.services;

import org.ecommersebe.ecommersebe.models.entities.Order;
import org.ecommersebe.ecommersebe.models.entities.OrderDetail;
import org.ecommersebe.ecommersebe.models.entities.User;
import org.ecommersebe.ecommersebe.models.payload.dto.order.CartItem;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> create(List<CartItem> cartItems, Order order);
}
