package com.Farm.NASMS.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class LoanPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private BigDecimal interestRate;
    private int durationMonths;
    private BigDecimal monthlyPenalty;
    private double minimumFarmSize;
    private double maximumFarmSize;
    private String description;

    @ManyToOne
    private FarmingSeason farmingSeason;

    public LoanPackage(){}
}
