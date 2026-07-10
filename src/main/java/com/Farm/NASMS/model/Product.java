package com.Farm.NASMS.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double quantityUnit;
    private double unitPrice_ksh;

    @ManyToOne
    private Farmer farmer;
}


