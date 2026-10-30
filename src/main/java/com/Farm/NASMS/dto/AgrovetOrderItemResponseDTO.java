package com.Farm.NASMS.dto;

import java.math.BigDecimal;

public record AgrovetOrderItemResponseDTO(Long id,
                                          Long productId,
                                          String productName,
                                          BigDecimal quantity,
                                          String unit,
                                          BigDecimal unitPrice,
                                          BigDecimal subtotal
) {}

