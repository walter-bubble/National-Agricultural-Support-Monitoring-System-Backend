package com.Farm.NASMS.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class MarketTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private FarmingSeason season;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String sellerType;
    private String sellerName;
    private Long sellerId;

    private String buyerName;
    private Long buyerId;

    private String productName;
    private String productCode;
    private double quantityRequested;
    private double price;

    private LocalDateTime transactionDate = LocalDateTime.now();

    public MarketTransaction(){}

    public MarketTransaction(String sellerType, String sellerName, String buyerName, String productName, double quantity, double price,String productCode){
        this.sellerType=sellerType;
        this.sellerName=sellerName;
        this.buyerName=buyerName;
        this.productName=productName;
        this.quantityRequested=quantityRequested;
        this.price=price;
        this.productCode=productCode;
    }

    public MarketTransaction(String sellerName, String sellerType, String productName, String productCode, String buyerName, double quantityRequested, double totalInvoiceCost) {
    }
}
