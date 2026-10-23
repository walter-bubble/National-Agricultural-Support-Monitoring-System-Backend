package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.AgrovetOperationalStatus;
import com.Farm.NASMS.enums.AgrovetProductCategory;
import com.Farm.NASMS.enums.AgrovetStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AdminAgrovetResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String emailAddress;
    private String businessName;
    private String location;
    private String accountNumber;
    private Set<AgrovetProductCategory> productsOffered;
    private AgrovetStatus status;
    private AgrovetOperationalStatus operationalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}
