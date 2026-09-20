package com.Farm.NASMS.dto;

import java.math.BigDecimal;

public class MpesaStkRequest {
    private BigDecimal amount;
    private String phoneNumber;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
