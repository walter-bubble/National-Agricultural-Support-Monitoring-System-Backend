package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.ProductOrderStatusN;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ProduceDeliveryN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Farmer farmer;

    private String orderCode;
    private String productNam;
    private double quantityKg;
    private LocalDateTime orderDate;
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    private ProductOrderStatusN status;
}
