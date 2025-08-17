package org.ecommersebe.ecommersebe.repositories;


import org.ecommersebe.ecommersebe.models.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
