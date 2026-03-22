package com.Farm.NASMS;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import javax.annotation.processing.Generated;

@Entity
@Table(name="users",uniqueconstraints={@UniqueConstraint(columnNames = "userName")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;
    private String role;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

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
