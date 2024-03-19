package org.example;

import java.io.FileWriter;
import java.io.IOException;

abstract public class Vehicle {
    public String brand, model;
    public Integer year, price, index;
    public Boolean rented;
    public Vehicle(String brand, String model, Integer year, Integer price,Boolean rented, Integer index){
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.index = index;
        this.rented = rented;
    }
    public String toCSV(){
       // try (FileWriter writer = new FileWriter("Vehicles.csv", true)){
            //writer.write(String.format("%s \n %s \n %d \n %d \n %s \n %d", brand, model, year, price, rented, index));
            return String.format(String.format("%s,%s,%d,%d,%s,%d%n", brand, model, year, price, rented, index));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public String toString(){
        return String.format(String.format("%s,%s,%d,%d,%s,%d%n", brand, model, year, price, rented, index));
    }
}
