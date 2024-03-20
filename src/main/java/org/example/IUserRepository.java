package org.example;

import java.util.List;

public interface IUserRepository {
    List<User> getUsers();
    User getUser(String login);
    public void save(List<User> users);
    void save(User user);
}
