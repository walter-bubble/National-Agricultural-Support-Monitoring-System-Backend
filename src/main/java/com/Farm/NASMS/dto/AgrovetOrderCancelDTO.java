package com.Farm.NASMS.dto;

import jakarta.validation.constraints.NotBlank;

public record AgrovetOrderCancelDTO(@NotBlank(message = "Cancellation reason is required")
                                      String reason) {
}
