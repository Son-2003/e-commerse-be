package org.ecommersebe.ecommersebe.services.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.entities.Order;
import org.ecommersebe.ecommersebe.models.entities.OrderDetail;
import org.ecommersebe.ecommersebe.models.entities.Product;
import org.ecommersebe.ecommersebe.models.entities.User;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.models.payload.dto.order.CartItem;
import org.ecommersebe.ecommersebe.repositories.ProductRepository;
import org.ecommersebe.ecommersebe.services.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final ProductRepository productRepository;

    @Override
    public List<OrderDetail> create(List<CartItem> cartItems, Order order) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartItem item : cartItems){
            OrderDetail orderDetail = new OrderDetail();
            Product product = productRepository.findById(item.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", item.getId()));
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setTotalPrice(product.getPrice() * item.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
