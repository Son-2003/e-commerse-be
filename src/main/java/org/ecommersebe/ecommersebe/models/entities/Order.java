package org.ecommersebe.ecommersebe.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ecommersebe.ecommersebe.models.enums.OrderStatus;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"order\"")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private boolean isFeedback;

    @Column(nullable = false)
    private float totalAmount;

    @Column(nullable = false)
    private float amount;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    private PaymentHistory paymentHistory;

    @OneToMany(mappedBy = "order")
    private Set<Feedback> feedback;
}
