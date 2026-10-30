package com.Farm.NASMS.dto;

import java.math.BigDecimal;

public record FarmerInputRequestItemResponseDTO(
        Long productId,
        String productName,
        String category,
        BigDecimal quantity,
        String unit,
        Long preferredAgrovetId,
        String agrovetName
) {
}
