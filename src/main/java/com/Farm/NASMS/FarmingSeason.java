package com.Farm.NASMS;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

}
