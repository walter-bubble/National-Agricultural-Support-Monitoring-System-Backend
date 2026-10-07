package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.ProductOrderStatusN;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProduceDeliveryResponseN {
    private Long id;
    private String orderCode;
    private String productName;
    private double quantityKg;
    private LocalDateTime orderDate;
    private LocalDate scheduledDate;
    private ProductOrderStatusN status;
    public ProduceDeliveryResponseN(Long id,String orderCode,String productName,double quantityKg,LocalDateTime orderDate,LocalDate scheduledDate,ProductOrderStatusN status) {
        this.id = id;
        this.orderCode = orderCode;
        this.productName = productName;
        this.quantityKg = quantityKg;
        this.orderDate = orderDate;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getQuantityKg() {
        return quantityKg;
    }

    public void setQuantityKg(double quantityKg) {
        this.quantityKg = quantityKg;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public ProductOrderStatusN getStatus() {
        return status;
    }

    public void setStatus(ProductOrderStatusN status) {
        this.status = status;
    }
}
