package org.example;

public class Main {
    public static void main(String[] args) {
        UserRepository rep = new UserRepository();
        System.out.println(rep.login("zinton", "123"));
    }
}