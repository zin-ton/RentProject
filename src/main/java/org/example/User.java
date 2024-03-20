package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    String password, login;
    Integer index;
    boolean can = false;
    Idk wyp = new Idk();
    ArrayList <User> arr = new ArrayList<>();
    public User(){
        this.can = false;
    }
    public void readInfo(){
        try (BufferedReader reader = new BufferedReader(new FileReader("Vehicles.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                    User temp = new User();
                    temp.login = parts[0];
                    temp.password = parts[1];
                    if(parts.length == 3){
                        temp.index = Integer.parseInt(parts[2]);
                    }
                    arr.add(temp);
                }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }
    public String login(String login, String password){
        readInfo();
        for(int i = 0; i < arr.size(); ++i){
            if(arr.get(i).login.equals(login) && arr.get(i).password.equals(password)){
                this.can = true;
                this.login = login;
                this.password = password;
                return "sucesfull login";
            }
        }
        return "invalid login or password";
    }
    public String register(String login, String password){
        readInfo();
        for(int i = 0; i < arr.size(); ++i){
            if(arr.get(i).login.equals(login)){
                return "invalid login";
            }
        }
        this.login = login;
        this.password = password;
        arr.add(this);
        this.can = true;
        return "sucesfull login";
    }
    public void rentVehicle(Integer index){
        if(this.can){
            wyp.rentVehicle(index);
            this.index = index;
            this.can = false;
            readInfo();
            for(int i = 0; i < arr.size(); ++i){
                if(arr.get(i).login.equals(this.login)){
                    arr.get(i).index = this.index;
                    break;
                }
            }
            saveInfo();
            this.can = true;
        }
    }
    public void returnVehicle(Integer index){
        if(this.can){
            wyp.returnVehicle(index);
            this.index = -1;
            this.can = false;
            readInfo();
            for(int i = 0; i < arr.size(); ++i){
                if(arr.get(i).login.equals(this.login)){
                    arr.get(i).index = this.index;
                    break;
                }
            }
            saveInfo();
            this.can = true;
        }
    }
    public void showInfo(){
        readInfo();
        if(this.index == -1){
            System.out.println("login " + this.login + " password " + this.password + " currently you have no vehicles");
        }
        else{
          List<Vehicle> veh= wyp.getVehicles();
          for(int i = 0; i < veh.size(); ++i){
              if(this.index == veh.get(i).index){
                  System.out.println("login " + this.login + " password " + this.password + " vehicle info: " + veh.get(i).brand + " " +
                          veh.get(i).model + " " + veh.get(i).year + " " + veh.get(i).price + " " + veh.get(i).index);
              }
          }
        }
    }
    public void saveInfo(){
        for(int i = 0; i < arr.size(); ++i){
            try (FileWriter writer = new FileWriter("Users.csv", true)){
                //writer.write(String.format("%s \n %s \n %d \n %d \n %s \n %d", brand, model, year, price, rented, index));
                writer.write(String.format(String.format("%s,%s,%d,%n", arr.get(i).login, arr.get(i).password, arr.get(i).index)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
