package org.ecommersebe.ecommersebe.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stock")
public class StockTransaction extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockTransactionId;

    @Column(nullable = false)
    private LocalDateTime stockTransactionDate;

    @Column(nullable = false)
    private int quantityProduct;

    @Column(nullable = false)
    private int importQuantity;

    @Column(nullable = false)
    private float totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_Id", nullable = false)
    private Product product;
}
