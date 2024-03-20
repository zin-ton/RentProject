package org.example;

import java.util.List;

public interface IVehicleRepository {
    void rentVehicle(Integer index);
    void returnVehicle(Integer index);
    List<Vehicle> getVehicles();
    void save();
    void addVehicle(Car car);
    void addVehicle(Motorcycle motorcycle);
}
