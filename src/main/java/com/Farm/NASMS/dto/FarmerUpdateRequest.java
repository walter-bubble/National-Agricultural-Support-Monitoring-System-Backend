package com.Farm.NASMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record FarmerUpdateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "County is required")
        String county,

        @Positive(message = "Farm size must be greater than zero")
        Double farmSize,

        String titleNumber) {
}
