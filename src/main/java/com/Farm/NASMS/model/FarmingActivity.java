package com.Farm.NASMS.model;

import com.Farm.NASMS.enums.FarmingType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FarmingActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FarmingType farmingType;

    private String category;
    private String breedOrVariety;

    @ManyToOne
    private Farmer farmer;
}
