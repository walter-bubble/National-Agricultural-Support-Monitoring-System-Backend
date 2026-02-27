package com.Farm.NASMS;

import jakarta.persistence.*;

@Entity
@Table(name="farmers", uniqueConstraints = @UniqueConstraint(columnNames = "nationalId"))
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String name;
    private long nationalId;
    private String phoneNumber;
    private String email;
    private double farmSize;
    private String titleNumber;
    private String location;
    private String farmingType;
}
