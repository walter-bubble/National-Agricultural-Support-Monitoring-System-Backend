package com.Farm.NASMS.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FarmerInputRequestItemDTO(@NotNull(message = "Product ID is required")
Long agrovetProductId, @NotNull(message = "Quantity is required")
@DecimalMin(value = "0.001", message = "Quantity must be positive")
 BigDecimal quantity,
 @NotNull(message = "Please select an Agrovet")
         Long preferredAgrovetId
) {
}
