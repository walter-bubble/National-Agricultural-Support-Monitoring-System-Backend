package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.LoanStatus;
import com.Farm.NASMS.enums.LoanType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@JsonPropertyOrder({
        "farmerName",
        "nationalId",
        "seasonName",
        "loanId",
        "amount",
        "interestRate",
        "totalPayment",
        "remainingBalance",
        "dueDate",
        "status"
})
public class LoanResponse {
    private Long loanId;
    private String farmerName;
    private Long nationalId;
    private String seasonName;
    private BigDecimal amount;
    private double interestRate;
    private BigDecimal totalPayment;
    private BigDecimal remainingBalance;
    private LoanType loanType;
    private Long agrovetOrderId;
    private LoanStatus status;
    private LocalDateTime dueDate;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Long getNationalId() {
        return nationalId;
    }

    public void setNationalId(Long nationalId) {
        this.nationalId = nationalId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Long getAgrovetOrderId() {
        return agrovetOrderId;
    }

    public void setAgrovetOrderId(Long agrovetOrderId) {
        this.agrovetOrderId = agrovetOrderId;
    }
}
