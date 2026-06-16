package com.Farm.NASMS;

import jakarta.persistence.*;

@Entity
public class LoanPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loanId;
    private double amount;
    private double interestRate;
    private int durationMonths;
    private double monthlyPenalty;
    private String description;

    @ManyToOne
    @JoinColumn(name="season_id")
    private FarmingSeason farmingSeason;

    public LoanPackage(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public double getMonthlyPenalty() {
        return monthlyPenalty;
    }

    public void setMonthlyPenalty(double monthlyPenalty) {
        this.monthlyPenalty = monthlyPenalty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FarmingSeason getFarmingSeason() {
        return farmingSeason;
    }

    public void setFarmingSeason(FarmingSeason farmingSeason) {
        this.farmingSeason = farmingSeason;
    }
}
