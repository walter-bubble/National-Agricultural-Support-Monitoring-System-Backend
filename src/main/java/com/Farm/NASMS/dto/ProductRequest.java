package com.Farm.NASMS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class ProductRequest {
    private Long id;
    private String name;
    private double quantityRequested;
    private double unitPrice;
    private Long farmerId;
}
