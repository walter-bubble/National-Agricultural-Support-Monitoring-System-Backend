package com.Farm.NASMS.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class FarmingSeason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seasonName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean closed=false;
    private double budget;
    public boolean isActive(){
        LocalDate today = LocalDate.now();
        if(closed || startDate == null || endDate==null){
            return false;
        }
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
    public boolean shouldAutoClose(){
        return LocalDate.now().isAfter(endDate);
    }

}
