package org.example;

public interface IVehicleRepository {
    void rentVehicle(Integer index);
    void returnVehicle(Integer index);
    void getVehicles();
    void save();
}
