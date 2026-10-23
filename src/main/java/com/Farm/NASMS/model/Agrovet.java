package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.AgrovetOperationalStatus;
import com.Farm.NASMS.enums.AgrovetProductCategory;
import com.Farm.NASMS.enums.AgrovetStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "agrovets")
@Data
public class Agrovet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //relationship
    @OneToOne(optional = false)
    @JoinColumn(name="user_id",nullable = false,unique = true)
    private User user;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String accountNumber;

    @ElementCollection
    @CollectionTable(name="agrovet_products_offered",joinColumns = @JoinColumn(name="agrovet_id"))
    @Enumerated(EnumType.STRING)
    @Column(name="product_category",nullable = false)
    private Set<AgrovetProductCategory> productsOffered = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgrovetStatus status = AgrovetStatus.PENDING_APPROVAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgrovetOperationalStatus operationalStatus = AgrovetOperationalStatus.INACTIVE;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime reviewedAt;
}
