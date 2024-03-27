package org.example.authenthicate;

import com.google.common.hash.Hashing;
import org.example.dao.jdbc.JdbcUserRepository;
import org.example.model.User;

import java.nio.charset.Charset;

public class Authenticator {
    public static User login(String login, String password){
        JdbcUserRepository jur = JdbcUserRepository.getInstance();
        User userFromDb = jur.getUser(login);
        if ( userFromDb!= null && hashPassword(password).equals(userFromDb.getPassword())) {
            return userFromDb;
        }
        return null;
    }

    public static String hashPassword(String password){
        return (Hashing.sha256().hashString(password, Charset.defaultCharset()).toString());
    }

}
