package com.Farm.NASMS.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walter.toolkit.security_starter.user.SecurityUser;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames = "user_name")})
public class User implements SecurityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username cannot be blank")
    @Column(name="user_name")
    private String userName;

    @NotBlank(message="userEmail cannot be blank")
    @Column(name="email_address")
    private String emailAddress;

    @JsonIgnore
    @NotBlank(message = "password cannot be blank")
    private String password;

    private String role;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Override
    public Collection<String> getRoles() {
        return role != null && !role.isBlank() ? List.of(role) : List.of();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
