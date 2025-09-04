package org.ecommersebe.ecommersebe.repositories;

import org.ecommersebe.ecommersebe.models.entities.Feedback;
import org.ecommersebe.ecommersebe.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
    List<Feedback> findByOrderId(Long orderId);
}
