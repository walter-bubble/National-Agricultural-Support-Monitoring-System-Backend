package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.ProductOrderStatusN;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
public class ProduceDeliveryN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Farmer farmer;

    private String orderCode;
    private String productName;
    private double quantityKg;
    private LocalDateTime orderDate;
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    private ProductOrderStatusN status;
}
