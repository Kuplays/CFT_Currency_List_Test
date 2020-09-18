package com.cft.kuplays.currency;

public class CurrencyItem {
    private String id;
    private String numCode;
    private String charCode;
    private int nominal;
    private String name;
    private double value;
    private double previousValue;
    private double displayValue;
    private double displayPreviousValue;
    private double percentageChange;
    private boolean hasIncreased;

    public CurrencyItem(String id,
                        String numCode,
                        String charCode,
                        int nominal,
                        String name,
                        double value,
                        double previousValue) {
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previousValue = previousValue;
        this.percentageChange = 0.0;
        this.hasIncreased = this.value > this.previousValue;
        this.displayValue = value;
        this.displayPreviousValue = previousValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public String getPercentageChangeFormat() {
        return String.format("%.2f %%", this.percentageChange);
    }

    public void setPercentageChange() {
        double a, b, newChange;
        if (this.value > this.previousValue) {
            b = this.value;
            a = this.previousValue;
        } else {
            b = this.previousValue;
            a = this.value;
        }

        newChange = (b - a) / a * 100.0;
        this.percentageChange = newChange;
    }

    public boolean isHasIncreased() {
        return hasIncreased;
    }

    public void setHasIncreased(boolean hasIncreased) {
        this.hasIncreased = hasIncreased;
    }

    public double getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(double displayValue) {
        this.displayValue = displayValue;
    }

    public double getDisplayPreviousValue() {
        return displayPreviousValue;
    }

    public void setDisplayPreviousValue(double displayPreviousValue) {
        this.displayPreviousValue = displayPreviousValue;
    }

    @Override
    public String toString() {
        return "CurrencyItem{" +
                "id='" + id + '\'' +
                ", numCode='" + numCode + '\'' +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", previousValue=" + previousValue +
                '}';
    }
}
