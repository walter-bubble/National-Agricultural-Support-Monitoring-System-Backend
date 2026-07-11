package com.Farm.NASMS.dto;

public class LoginRequest {
    private String emailAddress;
    private String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmail(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
