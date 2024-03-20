package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class User {
    String password, login;
    Integer index;
    boolean can = false;
    Idk wyp = new Idk();
    public User(){
        this.can = false;
    }
    public String login(String login, String password){
        try (BufferedReader reader = new BufferedReader(new FileReader("Vehicles.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(login.equals(parts[0]) && password.equals(parts[1])){

                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }
    public void rentVehicle(Integer index){
        wyp.rentVehicle(index);
        this.index = index;
        saveInfo();
    }
    public void returnVehicle(Integer index){
        wyp.returnVehicle(index);
        this.index = -1;
        saveInfo();
    }
    public void showInfo(){

    }
    public void saveInfo(){

    }
}
