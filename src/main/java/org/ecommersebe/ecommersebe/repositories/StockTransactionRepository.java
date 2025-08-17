package org.ecommersebe.ecommersebe.repositories;


import org.ecommersebe.ecommersebe.models.entities.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
}
