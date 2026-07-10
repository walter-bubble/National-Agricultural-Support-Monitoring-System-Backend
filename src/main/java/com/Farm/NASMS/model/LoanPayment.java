package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="loan_id",nullable = false)
    private Loan loan;

    @Positive(message = "Amount to pay must be greater than zero!")
    private double amountToPay;

    private double remainingBalance;
    private double previousTotal;

    private double TotalAmountPaid;

    @Column(updatable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(unique = true)
    private String transactionCode;

    @PrePersist
    protected void onCreate(){
        paymentDate=LocalDateTime.now();
    }
}
