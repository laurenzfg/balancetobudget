package org.studieren_ohne_grenzen.balancetobudget;

public class Category {
    private String name;
    private int amount;

    public Category (String name) {
        this.name = name;
    }

    public void add (int amount) {
        this.amount += amount; 
    }

    public String getName() {
        return name;
    }

    public int getAmount () {
        return amount;
    }
    // JAVA Number Format?
    public String getGermanAmount () {
        int cents = amount % 100;
        int euros = (amount - cents) / 100;
        return euros + "," + cents;
    }

    @Override
    public String toString () {
        return name + ": " + amount;
    }
}