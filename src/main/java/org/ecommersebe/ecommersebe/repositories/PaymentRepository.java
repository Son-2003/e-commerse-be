package org.ecommersebe.ecommersebe.repositories;


import org.ecommersebe.ecommersebe.models.entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentHistory, Long> {
}
