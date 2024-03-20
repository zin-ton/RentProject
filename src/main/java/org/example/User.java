package org.example;

import com.google.common.hash.Hashing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class User {
    String login;
    String password;
    String role;
    Integer index;
    UserRepository rep = new UserRepository();
    public User(){
        role = null;
    }
}
