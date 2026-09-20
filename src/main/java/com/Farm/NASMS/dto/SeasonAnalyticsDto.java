package com.Farm.NASMS.dto;

import java.math.BigDecimal;

public class SeasonAnalyticsDto {
    private String seasonName;
    private Long totalFarmers;
    private Long totalLoans;
    private Long approvedLoans;
    private Long rejectedLoans;
    private BigDecimal totalSupportFunds;
    private BigDecimal totalSales;
    private BigDecimal totalLoanAmount;
    private BigDecimal remainingBudget;
    private double loanUtilizationRate;

    public SeasonAnalyticsDto(){

    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public Long getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(Long totalFarmers) {
        this.totalFarmers = totalFarmers;
    }

    public Long getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(Long totalLoans) {
        this.totalLoans = totalLoans;
    }

    public BigDecimal getTotalSupportFunds() {
        return totalSupportFunds;
    }

    public void setTotalSupportFunds(BigDecimal totalSupportFunds) {
        this.totalSupportFunds = totalSupportFunds;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public Long getApprovedLoans() {
        return approvedLoans;
    }

    public void setApprovedLoans(Long approvedLoans) {
        this.approvedLoans = approvedLoans;
    }

    public Long getRejectedLoans() {
        return rejectedLoans;
    }

    public void setRejectedLoans(Long rejectedLoans) {
        this.rejectedLoans = rejectedLoans;
    }

    public BigDecimal getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(BigDecimal remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public double getLoanUtilizationRate() {
        return loanUtilizationRate;
    }

    public void setLoanUtilizationRate(double loanUtilizationRate) {
        this.loanUtilizationRate = loanUtilizationRate;
    }
}
