package org.ecommersebe.ecommersebe.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "feedback")
public class Feedback extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = true)
    private String comment;

    @Column(nullable = true, length = Length.LOB_DEFAULT)
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_Id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_Id", nullable = false)
    private Order order;
}
