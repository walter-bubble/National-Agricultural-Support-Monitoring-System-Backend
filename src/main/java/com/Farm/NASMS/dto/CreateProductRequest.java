package com.Farm.NASMS.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateProductRequest {
    @NotNull
    private String productName;

    @NotNull
    private String description;

    @NotNull
    @DecimalMin(value="0.01")
    @Digits(integer = 10,fraction = 2)
    private BigDecimal price;

    @NotNull
    @DecimalMin(value="0.001")
    @Digits(integer=10,fraction = 3)
    private BigDecimal quantityAvailable;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(BigDecimal quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
