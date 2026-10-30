package com.Farm.NASMS.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FarmerInputRequestResponseDTO(
        Long requestId,
        String farmerName,
        String farmerEmail,
        String status,
        LocalDateTime createdAt,
        List<FarmerInputRequestItemResponseDTO> items
) {
}
