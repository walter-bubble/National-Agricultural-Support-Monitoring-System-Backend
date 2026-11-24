package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class LoanPaymentRequest {
    @NotNull(message="amount to pay is required")
    @DecimalMin(value="1.00", message="amount to pay must be at least ksh 1.00")
    private BigDecimal amountToPay;

    @NotNull(message="payment method required")
    private PaymentMethod paymentMethod;

    @NotBlank(message="transaction reference code is required")
    private String transactionCode;

    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(BigDecimal amountToPay) {
        this.amountToPay = amountToPay;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}
