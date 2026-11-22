package com.Farm.NASMS.dto;

import com.Farm.NASMS.model.FarmingSeason;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FarmingSeasonResponse(
        Long id,
        String seasonName,
        BigDecimal budget,
        LocalDate startDate,
        LocalDate endDate,
        boolean closed,
        boolean active
) {
    public static FarmingSeasonResponse from(FarmingSeason season) {
        return new FarmingSeasonResponse(
                season.getId(),
                season.getSeasonName(),
                season.getBudget(),
                season.getStartDate(),
                season.getEndDate(),
                season.isClosed(),
                season.isActive()
        );
    }
}
