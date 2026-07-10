package com.Farm.NASMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames = "user_name")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username cannot be blank")
    @Column(name="user_name")
    private String userName;

    @NotBlank(message="userEmail cannot be blank")
    @Column(name="email_address")
    private String emailAddress;

    @NotBlank(message = "password cannot be blank")
    private String password;
    private String role;
}
