package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.PaymentMethod;

import java.math.BigDecimal;

public class LoanPaymentRequest {
    private BigDecimal amountToPay;
    private PaymentMethod paymentMethod;
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
