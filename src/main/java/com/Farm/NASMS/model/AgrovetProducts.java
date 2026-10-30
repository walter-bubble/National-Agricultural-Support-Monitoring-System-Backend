package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="agrovet_products")
@Data
public class AgrovetProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="agrovet_id",nullable = false)
    private Agrovet agrovet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgrovetProductCategory category;

    @Column(nullable = false,length = 50)
    private String productName;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal initialQuantity;

    @Column(nullable = false)
    private BigDecimal quantityAvailable;

    @Column(nullable = false,precision = 12,scale = 2)
    private BigDecimal price;

    @Column(nullable = false,length = 20)
    private String unit;

    @Column(nullable = false)
    private boolean active=true;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    @PreUpdate
    protected void  onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
