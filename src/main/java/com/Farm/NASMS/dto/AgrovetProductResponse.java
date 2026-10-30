package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AgrovetProductResponse {
    private Long id;
    private Long agrovetId;
    private String agrovetName;
    private String productName;
    private AgrovetProductCategory category;
    private String brand;
    private String description;
    private BigDecimal initialQuantity;
    private BigDecimal quantityAvailable;
    private BigDecimal price;
    private String unit;
    private String stockStatus;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

