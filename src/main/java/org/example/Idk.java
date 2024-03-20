package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Idk implements IVehicleRepository{
    List<Vehicle> vehicles = new ArrayList<>();
    @Override
    public void rentVehicle(Integer index) {
        vehicles = new ArrayList<>();
        getVehicles();
        for(int i = 0; i < vehicles.size(); ++i){
            Vehicle temp = vehicles.get(i);
            if(temp.index == index){
                temp.rented = true;
                vehicles.set(i, temp);
            }
        }
        save();
    }

    @Override
    public void returnVehicle(Integer index) {
        vehicles = new ArrayList<>();
        getVehicles();
        for(int i = 0; i < vehicles.size(); ++i){
            Vehicle temp = vehicles.get(i);
            if(temp.index == index){
                temp.rented = false;
                vehicles.set(i, temp);
            }
        }
        save();
    }

    @Override
    public List<Vehicle> getVehicles() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Vehicles.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String brand = parts[0];
                String model = parts[1];
                int year = Integer.parseInt(parts[2]);
                int price = Integer.parseInt(parts[3]);
                boolean rented = Boolean.parseBoolean(parts[4]);
                int index = Integer.parseInt(parts[5]);
                vehicles.add(new Car(brand, model, year, price, rented, index));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
        return vehicles;
    }

    @Override
    public void save() {
        for(int i = 0; i < vehicles.size(); ++i){
            try (FileWriter writer = new FileWriter("Vehicles.csv", true)){
                Vehicle temp = vehicles.get(i);
                writer.write(String.format(String.format("%s,%s,%d,%d,%s,%d%n", temp.brand, temp.model, temp.year, temp.price, temp.rented, temp.index)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addVehicle(Car car) {
        vehicles.add(car);
        save();
    }

    @Override
    public void addVehicle(Motorcycle motorcycle) {
        vehicles.add(motorcycle);
        save();
    }
}
