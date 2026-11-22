package com.Farm.NASMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FarmingSeasonRequest(
        @NotBlank(message = "Season name is required")
        String seasonName,

        @NotNull(message = "Budget is required")
        @Positive(message = "Budget must be greater than zero")
        BigDecimal budget,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        Boolean closed
) {
}
