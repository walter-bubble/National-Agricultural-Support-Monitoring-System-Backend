package com.Farm.NASMS;

public class SeasonComparisonDto {
    private String season1;
    private String season2;

    private double salesGrowthPercentage;
    private double farmersGrowthPercentage;
    private double loansGrowthPercentage;

    public String getSeason1() {
        return season1;
    }

    public void setSeason1(String season1) {
        this.season1 = season1;
    }

    public String getSeason2() {
        return season2;
    }

    public void setSeason2(String season2) {
        this.season2 = season2;
    }

    public double getSalesGrowthPercentage() {
        return salesGrowthPercentage;
    }

    public void setSalesGrowthPercentage(double salesGrowthPercentage) {
        this.salesGrowthPercentage = salesGrowthPercentage;
    }

    public double getFarmersGrowthPercentage() {
        return farmersGrowthPercentage;
    }

    public void setFarmersGrowthPercentage(double farmersGrowthPercentage) {
        this.farmersGrowthPercentage = farmersGrowthPercentage;
    }

    public double getLoansGrowthPercentage() {
        return loansGrowthPercentage;
    }

    public void setLoansGrowthPercentage(double loansGrowthPercentage) {
        this.loansGrowthPercentage = loansGrowthPercentage;
    }
}
