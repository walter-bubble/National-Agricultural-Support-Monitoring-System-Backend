package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "agrovet_loans")
public class AgrovetLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Farmer who requested the products
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    // Agrovet selected by the farmer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agrovet_id", nullable = false)
    private Agrovet agrovet;

    // Products requested under this loan
    @OneToMany(mappedBy = "agrovetLoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgrovetLoanItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
