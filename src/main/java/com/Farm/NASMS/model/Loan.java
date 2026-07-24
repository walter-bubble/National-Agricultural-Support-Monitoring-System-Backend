package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Data
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//for loan records

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id")//connect to farmer table
    private Farmer farmer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="season_id")
    private FarmingSeason farmingSeason;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="loan_package_id")
    private LoanPackage loanPackage;

    @OneToMany(mappedBy = "loan",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LoanPayment> loanPayments;

    //loan details
    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double interestRate;

    @Column(nullable = false)
    private int durationMonths;

    private double monthlyPenalty;
    private double totalPayment;
    private LocalDateTime issuedDate;
    private LocalDateTime dueDate;
    private double remainingBalance;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    //Audit
    private LocalDateTime createdAt;

    //the life cycle
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        if(issuedDate==null){
            issuedDate=LocalDateTime.now();
        }
    }
    //business logic here
    public Loan(){}
    public static Loan createLoanFromPackage(Farmer farmer, LoanPackage loanPackage) {
        Loan loan = new Loan();
        loan.setFarmer(farmer);

        loan.setAmount(loanPackage.getAmount());
        loan.setInterestRate(loanPackage.getInterestRate());
        loan.setDurationMonths(loanPackage.getDurationMonths());
        loan.setMonthlyPenalty(loanPackage.getMonthlyPenalty());
        loan.setTotalPayment(calculateTotalPayment(loan));

        LocalDateTime now = LocalDateTime.now();
        loan.setIssuedDate(now);
        loan.setDueDate(now.plusMonths(loanPackage.getDurationMonths()));
        loan.setStatus(LoanStatus.ACTIVE);
        return loan;
    }
    private static double calculateTotalPayment(Loan loan){
        return loan.getAmount() + (loan.getAmount()*loan.getInterestRate()/100*loan.getDurationMonths()/12);
    }
    public double getRemainingBalance(){
        double paid=(loanPayments==null) ? 0:
                loanPayments.stream().mapToDouble(LoanPayment:: getTotalAmountPaid).sum();
        return totalPayment-paid;
    }
    public double calculatePenalty(LocalDateTime dueDate){
        if(dueDate != null && dueDate.isAfter(this.dueDate)){
            long monthsLate = ChronoUnit.MONTHS.between(this.dueDate,dueDate);
            return monthsLate * monthlyPenalty;
        }
        return 0;
    }
    public double calculateTotalDue(LocalDateTime paymentDate){
        return this.totalPayment + calculatePenalty(paymentDate);
    }
    public boolean isOverDue(){
        return LocalDateTime.now().isAfter(dueDate) && getRemainingBalance()>0;
    }
}