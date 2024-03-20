package org.example;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Authentication {
    UserRepository rep = new UserRepository();
    public boolean checkCredentials(String login, String password){
        User user = rep.getUser(login);
        if(user == null) return false;
        else{
            String passwordHash = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();
            if(passwordHash == user.password) return true;
        }
        return false;
    }
}
