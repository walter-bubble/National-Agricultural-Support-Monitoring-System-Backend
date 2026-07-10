package com.Farm.NASMS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class MarketListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;
    private String sellerName;
    private Long sellerId;
    private String sellerType;
    private String productName;
    private double quantity;
    private double price;

    private LocalDateTime created = LocalDateTime.now();
    public MarketListing(String productCode,String sellerName,Long sellerId,String sellerType,String productName,double quantity,double price){
        this.sellerName=sellerName;
        this.sellerId=sellerId;
        this.sellerType=sellerType;
        this.productName=productName;
        this.quantity=quantity;
        this.price=price;
    }

    public MarketListing(){}
}
