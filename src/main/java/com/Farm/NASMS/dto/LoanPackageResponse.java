package com.Farm.NASMS.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanPackageResponse(
        Long id,
        BigDecimal amount,
        BigDecimal interestRate,
        Integer durationMonths,
        BigDecimal monthlyPenalty,
        String description,
        Double minimumFarmSize,
        Double maximumFarmSize,
        Long seasonId,
        String seasonName,
        LocalDate seasonStartDate,
        LocalDate seasonEndDate
) {
}
