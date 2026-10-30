package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgrovetProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    private String productName;

    @NotNull(message = "Category is required")
    private AgrovetProductCategory category;

    @Size(max = 100)
    private String brand;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.000", inclusive = false, message = "Quantity must be greater than zero")
    private BigDecimal quantityAvailable;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "Unit is required")
    @Size(max = 20)
    private String unit;
}
