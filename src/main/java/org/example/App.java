package org.example;

import org.example.authenthicate.Authenticator;
import org.example.dao.IUserRepository;
import org.example.dao.IVehicleRepository;
import org.example.dao.jdbc.JdbcUserRepository;
import org.example.dao.jdbc.JdbcVehicleRepository;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.model.Vehicle;

import java.util.Scanner;

import static org.example.authenthicate.Authenticator.hashPassword;

public class App {
    public static User user = null;
    private final Scanner scanner = new Scanner(System.in);
    private final IUserRepository iur = JdbcUserRepository.getInstance();
    private final IVehicleRepository ivr = JdbcVehicleRepository.getInstance();

    public void run() {

        System.out.println("LOG IN");

        user = Authenticator.login(scanner.nextLine(), scanner.nextLine());
        if (user != null) {
            System.out.println("logged in!!");

            String response = "";
            boolean running = true;
            while (running) {

                System.out.println("-----------MENU------------");
                System.out.println("00 - show info");
                System.out.println("0 - show cars");
                System.out.println("1 - rent car");
                System.out.println("2 - return car");
                System.out.println("4 - get car");
                System.out.println("6 - add car");
                System.out.println("7 - remove car");
                System.out.println("8 - add user");
                System.out.println("9 - remove user");
                response = scanner.nextLine();
                switch (response) {
                    case "00":
                        System.out.println(user);
                        break;
                    case "0":
                        for (Vehicle v : ivr.getVehicles()) {
                            System.out.println(v);
                        }
                        break;
                    case "1":
                        System.out.println("plates:");
                        String plate = scanner.nextLine();
                        ivr.rentVehicle(plate, user.getLogin());
                        user = iur.getUser(user.getLogin());
                        break;
                    case "2":
                        ivr.returnVehicle(user.getRentedPlate(), user.getLogin());
                        break;
                    case "4":
                        System.out.println("plate:");
                        System.out.println(ivr.getVehicle(scanner.nextLine()));
                        break;
                    case "6":
                        System.out.println("add car (only) separator is ; ");
                        ////Motorcycle(String brand, String model, int year, double price, String plate, String category)
                        String line = scanner.nextLine();
                        String[] arr = line.split(";");
                        if (arr.length == 5) {
                            ivr.addVehicle(
                                    new Car(arr[0],
                                            arr[1],
                                            Integer.parseInt(arr[2]),
                                            Double.parseDouble(arr[3]),
                                            arr[4]));
                        }
                        if (arr.length == 6) {
                            ivr.addVehicle(
                                    new Motorcycle(arr[0],
                                            arr[1],
                                            Integer.parseInt(arr[2]),
                                            Double.parseDouble(arr[3]),
                                            arr[4],
                                            arr[5]));
                        }
                        break;
                    case "7":
                        System.out.println("remove car:");
                        String removePlate = scanner.nextLine();
                        ivr.removeVehicle(removePlate);
                        break;
                    case "8":
                        System.out.println("add user (only) separator is ; ");
                        String line2 = scanner.nextLine();
                        String[] arr2 = line2.split(";");
                        iur.addUser(
                                new User(arr2[0],
                                        hashPassword(arr2[1]),
                                        User.Role.valueOf(arr2[2]),
                                        null));
                        break;
                    case "9":
                        System.out.println("remove user:");
                        String removeLogin = scanner.nextLine();
                        iur.removeUser(removeLogin);
                        break;

                    default:
                        running = false;
                }
            }
        } else {
            System.out.println("Bledne dane!");
        }
        System.exit(0);
    }
}