package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
public class MarketPlaceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Farmer seller;

    @Column(nullable = false,length = 150)
    private String productName;
    @Column(nullable = false,precision = 12,scale=2)
    private BigDecimal price;
    @Column(nullable = false,precision = 12,scale = 3)
    private BigDecimal quantityAvailable;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
