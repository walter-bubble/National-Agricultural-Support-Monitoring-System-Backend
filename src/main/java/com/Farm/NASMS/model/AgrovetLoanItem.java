package com.Farm.NASMS.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "agrovet_loan_items")
public class AgrovetLoanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agrovet_loan_id", nullable = false)
    private AgrovetLoan agrovetLoan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agrovet_product_id", nullable = false)
    private AgrovetProducts agrovetProducts;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    // Snapshot of the price when the loan is requested
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

}
