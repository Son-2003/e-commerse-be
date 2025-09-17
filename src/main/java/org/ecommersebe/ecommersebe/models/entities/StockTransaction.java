package org.ecommersebe.ecommersebe.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
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
    private float unitPrice;

    @Column(nullable = false)
    private float totalPrice;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private String importedBy;

    @Column(nullable = false)
    private EntityStatus status;

    @Column(nullable = false)
    private String imageEvidence;

    @Column(nullable = false)
    private String imageInvoice;

    @Column(nullable = false)
    private String note;

    @ManyToOne
    @JoinColumn(name = "product_Id", nullable = false)
    private Product product;
}
