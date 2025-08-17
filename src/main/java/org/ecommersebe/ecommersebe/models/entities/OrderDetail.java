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
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private float unitPrice;

    @Column(nullable = false)
    private float totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_Id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_Id", nullable = false)
    private Order order;
}
