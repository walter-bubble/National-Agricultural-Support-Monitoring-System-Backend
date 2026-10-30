package com.Farm.NASMS.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FarmerInputRequestCreateDTO(@NotEmpty(message = "Select at least one product")
                                          List<@Valid FarmerInputRequestItemDTO> items) {
}
