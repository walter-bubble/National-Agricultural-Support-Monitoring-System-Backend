package com.Farm.NASMS;

public class User {
    private Long id;
    private String userName;
    private String password;
    private String role;
    public void setRole(String role) {
        this.role = role;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
