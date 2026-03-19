package com.Farm.NASMS;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//for loan records

    @ManyToOne
    @JoinColumn(name = "national_id")//connect to farmer table
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "loan_code")
    private LoanPackage loanPackage;

    private double amount;
    private double interestRate;
    private double monthlyPenalty;
    private int durationYears;
    private int durationMonths;
    private double totalPayment;
    private LocalDateTime paymentDate;
    private LocalDateTime issuedDate;
    private LocalDateTime dueDate;
    private String status;

    public Loan(){}
    public static Loan createLoanFromPackage(Farmer farmer, LoanPackage loanPackage) {
        Loan loan = new Loan();
        loan.setFarmer(farmer);
        loan.setTotalPayment(calculateTotalPayment(loan));
        loan.setLoanPackage(loanPackage);
        loan.setAmount(loanPackage.getAmount());
        loan.setInterestRate(loanPackage.getInterestRate());
        loan.setMonthlyPenalty(loan.getMonthlyPenalty());
        loan.setDurationYears(loanPackage.getDurationYears());
        loan.setDurationMonths(loanPackage.getDurationMonths());

        LocalDateTime now = LocalDateTime.now();
        loan.setIssuedDate(now);
        loan.setDueDate(now.plusYears(loanPackage.getDurationYears())
                .plusMonths(loanPackage.getDurationMonths()));
        loan.setStatus("Pending");
        return loan;
    }
    private static double calculateTotalPayment(Loan loan){
        return loan.getAmount() + (loan.getAmount()*loan.getInterestRate()/100);
    }
    public double calculatePenalty(LocalDateTime dueDate){
        if(paymentDate != null && paymentDate.isAfter(this.dueDate)){
            long monthsLate = ChronoUnit.MONTHS.between(this.dueDate,paymentDate);
            return monthsLate * monthlyPenalty;
        }
        return 0;
    }
    public double calculateTotalDue(LocalDateTime paymentDate){
        return this.totalPayment + calculatePenalty(paymentDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public LoanPackage getLoanPackage() {
        return loanPackage;
    }

    public void setLoanPackage(LoanPackage loanPackage) {
        this.loanPackage = loanPackage;
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

    public double getMonthlyPenalty() {
        return monthlyPenalty;
    }

    public void setMonthlyPenalty(double monthlyPenalty) {
        this.monthlyPenalty = monthlyPenalty;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
}