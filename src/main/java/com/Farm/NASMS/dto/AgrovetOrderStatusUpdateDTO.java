package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.AgrovetOrderStatus;
import jakarta.validation.constraints.NotNull;

public record AgrovetOrderStatusUpdateDTO(@NotNull(message = "Order status is required") AgrovetOrderStatus status) {
}
