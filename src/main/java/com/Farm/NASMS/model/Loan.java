package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.LoanStatus;
import com.Farm.NASMS.enums.LoanType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Data
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private FarmingSeason farmingSeason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_package_id")
    private LoanPackage loanPackage;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanPayment> loanPayments;

    @OneToOne
    @JoinColumn(name = "agrovet_order_id", unique = true)
    private AgrovetOrder agrovetOrder;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private int durationMonths;

    private BigDecimal monthlyPenalty;
    private BigDecimal totalPayment;
    private LocalDateTime dueDate;
    private BigDecimal remainingBalance;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType = LoanType.CASH_LOAN;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Loan() {
    }

    public static Loan createLoanFromPackage(Farmer farmer, LoanPackage loanPackage) {
        Loan loan = new Loan();
        loan.setFarmer(farmer);

        loan.setAmount(loanPackage.getAmount());
        loan.setInterestRate(loanPackage.getInterestRate());
        loan.setDurationMonths(loanPackage.getDurationMonths());
        loan.setMonthlyPenalty(loanPackage.getMonthlyPenalty());
        loan.setTotalPayment(calculateTotalPayment(loan));

        LocalDateTime now = LocalDateTime.now();
        loan.setDueDate(now.plusMonths(loanPackage.getDurationMonths()));
        loan.setStatus(LoanStatus.ACTIVE);
        return loan;
    }

    private static BigDecimal calculateTotalPayment(Loan loan) {
        BigDecimal interest = loan.getAmount()
                .multiply(loan.getInterestRate())
                .multiply(BigDecimal.valueOf(loan.getDurationMonths()))
                .divide(BigDecimal.valueOf(1200));
        return loan.getAmount().add(interest);
    }

    public BigDecimal getRemainingBalance() {
        if (remainingBalance != null) {
            return remainingBalance;
        }
        return totalPayment != null ? totalPayment : BigDecimal.ZERO;
    }

    public BigDecimal calculatePenalty(LocalDateTime paymentDate) {
        if (paymentDate != null && dueDate != null && paymentDate.isAfter(dueDate)) {
            long monthsLate = ChronoUnit.MONTHS.between(dueDate, paymentDate);
            return monthlyPenalty.multiply(BigDecimal.valueOf(monthsLate));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalDue(LocalDateTime paymentDate) {
        return totalPayment.add(calculatePenalty(paymentDate));
    }

    public boolean isOverDue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) && getRemainingBalance().compareTo(BigDecimal.ZERO) > 0;
    }
}