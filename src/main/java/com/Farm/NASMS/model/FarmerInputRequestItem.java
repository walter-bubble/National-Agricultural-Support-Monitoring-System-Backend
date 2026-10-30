package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name="farmer_input_request_items")
public class FarmerInputRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private FarmerInputRequest farmerInputRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "selected_product_id", nullable = false)
    private AgrovetProducts selectedProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_agrovet_id")
    private Agrovet preferredAgrovet;

    @Column(nullable = false, length = 150)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AgrovetProductCategory category;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal requestedQuantity;

    @Column(nullable = false, length = 20)
    private String unit;
}
