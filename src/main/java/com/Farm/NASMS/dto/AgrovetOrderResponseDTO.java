package com.Farm.NASMS.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AgrovetOrderResponseDTO(Long id,
                                      Long farmerInputRequestId,
                                      String farmerName,
                                      String farmerContact,
                                      String status,
                                      BigDecimal totalAmount,
                                      LocalDateTime forwardedAt,
                                      List<AgrovetOrderItemResponseDTO> items) {
}
