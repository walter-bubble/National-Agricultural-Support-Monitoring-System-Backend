package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class AgrovetApplicationRequest {
    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String emailAddress;

    @NotBlank
    @Size(min = 8,message="password must contain 8 characters")
    private String password;

    @NotBlank
    private String businessName;

    @NotBlank
    private String location;

    @NotBlank
    private String accountNumber;

    @NotEmpty(message = "select at least one product")
    private Set<AgrovetProductCategory>productsOffered;
}
