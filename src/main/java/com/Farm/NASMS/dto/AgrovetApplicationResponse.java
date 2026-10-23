package com.Farm.NASMS.dto;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import com.Farm.NASMS.enums.AgrovetStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AgrovetApplicationResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String emailAddress;
    private String businessName;
    private String location;
    private Set<AgrovetProductCategory> productsOffered;
    private AgrovetStatus status;
    private LocalDateTime createdAt;
}
