package com.Farm.NASMS.dto;

import lombok.Data;

@Data

public class ProductRequest {
    private String name;
    private double quantity;
    private double unitPrice;
    private Long farmerId;
}
