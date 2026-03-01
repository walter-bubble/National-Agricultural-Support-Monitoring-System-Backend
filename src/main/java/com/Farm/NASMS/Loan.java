package com.Farm.NASMS;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//for loan records

    @ManyToOne
    @JoinColumn(name = "farmer_id")//connect to farmer table
    private Farmer farmer;

    private double amount;
    private String status;
    private LocalDate issuedDate;
    private LocalDate dueDate;

    public void setStatus(String status) {
        this.status=status;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer=farmer;
    }
}


