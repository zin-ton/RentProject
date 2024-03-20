package org.example;

import com.google.common.hash.Hashing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements  IUserRepository{
    @Override
    public List<User> getUsers() {
        List <User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                User temp = new User();
                temp.login =  parts[0];
                temp.password = parts[1];
                temp.role = parts[2];
                temp.index = Integer.valueOf(parts[3]);
                users.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
        return users;
    }

    @Override
    public User getUser(String login) {
        List<User> users = getUsers();
        for(int i = 0; i < users.size(); ++i){
            if (users.get(i).login.equals(login)){
                return users.get(i);
            }
        }
        return null;
    }

    @Override
    public void save(List<User> users) {
        for(int i = 0; i < users.size(); ++i){
            try (FileWriter writer = new FileWriter("Users.csv", true)){
                User temp = users.get(i);
                writer.write(String.format(String.format("%s,%s,%s,%d%n", temp.login, temp.password, temp.role, temp.index)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void save(User user){
        List<User> users = getUsers();
        for(int i = 0; i < users.size(); ++i){
            if(users.get(i).login == user.login){
                users.set(i, user);
                save(users);
                return;
            }
        }
        users.add(user);
        save(users);
    }

    public User login(String login, String password){
        User temp = getUser(login);
        Authentication authentication = new Authentication();
        if(authentication.checkCredentials(login, password)){
            return temp;
        }
        else{
            return null;
        }
    }
    public void addVehicle(User user, Car car){
        Idk wyp = new Idk();
        if(user.role.equals("admin")){
            wyp.addVehicle(car);
        }
    }
    public void addVehicle(User user, Motorcycle motorcycle){
        Idk wyp = new Idk();
        if(user.role.equals("admin")){
            wyp.addVehicle(motorcycle);
        }
    }
    public List<User> getUsersAsAdmin(User user){
        if(user.role.equals("admin")){
            return getUsers();
        }
        else return null;
    }

    public void rentVehicle(User user, Integer index){
        Idk wyp = new Idk();
        if(user.role.equals("user")){
            user.index = index;
            wyp.rentVehicle(index);
            save(user);
        }
    }
    public void returnVehicle(User user, Integer index){
        Idk wyp = new Idk();
        if(user.role.equals("user")){
            user.index = 0;
            wyp.returnVehicle(index);
            save(user);
        }
    }
    public void getUserAsUser(User user){
        if(user.role.equals("user")) System.out.println(user.login + " " + user.password
        + " " + user.role + " " + user.index);
    }
}
