package com.Farm.NASMS;

public class SeasonAnalyticsDto {
    private String seasonName;
    private Long totalFarmers;
    private Long totalLoans;
    private Long totalLoansTaken;
    private double totalSupportFunds;
    private double totalSales;

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public Long getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(Long totalFarmers) {
        this.totalFarmers = totalFarmers;
    }

    public Long getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(Long totalLoans) {
        this.totalLoans = totalLoans;
    }

    public Long getTotalLoansTaken() {
        return totalLoansTaken;
    }

    public void setTotalLoansTaken(Long totalLoansTaken) {
        this.totalLoansTaken = totalLoansTaken;
    }

    public double getTotalSupportFunds() {
        return totalSupportFunds;
    }

    public void setTotalSupportFunds(double totalSupportFunds) {
        this.totalSupportFunds = totalSupportFunds;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}
