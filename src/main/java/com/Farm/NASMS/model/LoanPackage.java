package com.Farm.NASMS.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LoanPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private double interestRate;
    private int durationMonths;
    private double monthlyPenalty;
    private double minimumFarmSize;
    private double maximumFarmSize;
    private String description;

    @ManyToOne
    @JoinColumn(name="season_id")
    private FarmingSeason farmingSeason;

    public LoanPackage(){}
}
