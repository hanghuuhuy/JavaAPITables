package com.innoplus.javaapitables.domain;

public class MarketIndustry extends PersistentEntity {

    private Double currentMonthsOpeningPrice;
    private Double lastWeeksPrice;
    private String url;

    public MarketIndustry() {
    }

    public MarketIndustry(Double currentMonthsOpeningPrice, Double lastWeeksPrice) {
        this.lastWeeksPrice = lastWeeksPrice;
        this.currentMonthsOpeningPrice = currentMonthsOpeningPrice;
    }

    @Override
    public String toString() {
        return "Market url: id=" + this.url + "; lastWeeksPrice=" + this.lastWeeksPrice + "; currentMonthsOpeningPrice=" + this.currentMonthsOpeningPrice + ";";
    }

    public Double getCurrentMonthsOpeningPrice() {
        return currentMonthsOpeningPrice;
    }

    public void setCurrentMonthsOpeningPrice(Double currentMonthsOpeningPrice) {
        this.currentMonthsOpeningPrice = currentMonthsOpeningPrice;
    }

    public Double getLastWeeksPrice() {
        return lastWeeksPrice;
    }

    public void setLastWeeksPrice(Double lastWeeksPrice) {
        this.lastWeeksPrice = lastWeeksPrice;
    }
}
