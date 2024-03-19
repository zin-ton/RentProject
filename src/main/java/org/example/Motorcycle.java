package org.example;

public class Motorcycle extends Vehicle{
    Integer categorise;

    public Motorcycle(String brand, String model, Integer year, Integer price, Integer index, Boolean rented, Integer categorise) {
        super(brand, model, year, price,rented, index);
        this.categorise = categorise;
    }
}
