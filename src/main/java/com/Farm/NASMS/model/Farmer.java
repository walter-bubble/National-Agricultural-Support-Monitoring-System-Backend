package com.Farm.NASMS.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="farmers", uniqueConstraints = @UniqueConstraint(columnNames = "nationalId"))
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    private String name;
    @Column(unique=true,nullable = false)
    private Long nationalId;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique=true)
    private String email;

    private double farmSize;

    @Column(unique = true)
    private String titleNumber;

    private String county;
}
