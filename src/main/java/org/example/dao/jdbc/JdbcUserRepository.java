package org.example.dao.jdbc;

import org.example.dao.IUserRepository;
import org.example.dao.IVehicleRepository;
import org.example.model.User;
import org.postgresql.core.SqlCommand;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcUserRepository implements IUserRepository {
    private static JdbcUserRepository instance;
    private final DatabaseManager databaseManager;

    private static String GET_ALL_USERS_SQL = "SELECT login, password, role, rentedPlate FROM tuser";
    private static String GET_USER_SQL = "SELECT * FROM tuser WHERE login LIKE ?";
    private static String ADD_USER_SQL = "INSERT INTO tuser (login, password, role, rentedPlate) VALUES (?,?,?,NULL)";
    private static String DELETE_USER_SQL = "DELETE FROM tuser WHERE login LIKE ?";

    private JdbcUserRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }


    @Override
    public User getUser(String login) {
        User user = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role")),
                        rs.getString("rentedPlate")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        try{
            Connection connection = databaseManager.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_SQL);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());
            int changed = preparedStatement.executeUpdate();
            if (changed > 0) {
                connection.commit();
                System.out.println("User added");
            } else {
                connection.rollback();
                System.out.println("User not added");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUser(String login) {
        try {
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);
            preparedStatement.setString(1, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        @Override
        public Collection<User> getUsers () {
            Collection<User> users = new ArrayList<>();
            try (Connection connection = databaseManager.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {
                while (resultSet.next()) {
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    User.Role role = User.Role.valueOf(resultSet.getString("role"));
                    String plate = resultSet.getString("rentedPlate");

                    User user = new User(login, password, role, plate);
                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return users;
        }


        public static JdbcUserRepository getInstance () {
            if (JdbcUserRepository.instance == null) {
                JdbcUserRepository.instance = new JdbcUserRepository();
            }
            return instance;
        }

    }
