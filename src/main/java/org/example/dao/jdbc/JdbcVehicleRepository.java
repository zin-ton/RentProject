package org.example.dao.jdbc;

import org.example.dao.IVehicleRepository;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcVehicleRepository implements IVehicleRepository {
    private static JdbcVehicleRepository instance;
    private final DatabaseManager manager;
    private final String GET_ALL_VEHICLE_SQL = "SELECT * FROM tvehicle";

    private final String RENT_CAR_SQL = "UPDATE tvehicle SET rent = 1 WHERE plate LIKE ? AND rent = 0";
    private final  String RENT_UPDATE_USER_SQL = "UPDATE tuser SET rentedplate = ? WHERE login LIKE ? AND rentedplate IS NULL";
    private final String INSERT_SQL = "INSERT INTO tvehicle (brand, model, year, price, plate) VALUES (?,?,?,?,?)";

    private final String RETURN_CAR_SQL = "UPDATE tvehicle SET rent = 0 WHERE plate LIKE ? AND rent = 1";
    private final  String RETURN_UPDATE_USER_SQL = "UPDATE tuser SET rentedplate = NULL WHERE login LIKE ? AND rentedplate like ?";

    private final String DELETE_VEHICLE_SQL = "DELETE FROM tvehicle WHERE plate LIKE ?";

    private final String GET_VEHICLE_SQL = "SELECT * FROM tvehicle WHERE plate LIKE ?";


    public static JdbcVehicleRepository getInstance(){
        if (JdbcVehicleRepository.instance==null){
            instance = new JdbcVehicleRepository();
        }
        return instance;
    }

    private JdbcVehicleRepository() {
        this.manager = DatabaseManager.getInstance();
    }


    @Override
    public boolean rentVehicle(String plate, String login) {
        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = manager.getConnection();
            conn.setAutoCommit(false); // reczny commit

            rentCarStmt = conn.prepareStatement(RENT_CAR_SQL);
            rentCarStmt.setString(1, plate);
            int changed1 = rentCarStmt.executeUpdate();

            updateUserStmt = conn.prepareStatement(RENT_UPDATE_USER_SQL);
            updateUserStmt.setString(1, plate);
            updateUserStmt.setString(2, login);
            int changed2 =updateUserStmt.executeUpdate();

            if (changed1 > 0 && changed2 > 0) {
                System.out.println("rented");
                conn.commit();
            } else {
                System.out.println("not rented");
                conn.rollback(); // wycofuje zmiany
            }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { if (rentCarStmt != null) rentCarStmt.close(); } catch (Exception e) {};
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (Exception e) {};
            try { if (conn != null) conn.close(); } catch (Exception e) {};
        }
        return false;
    }

    @Override
    public boolean returnVehicle(String plate, String login) {
        Connection conn = null;
        PreparedStatement updateCarStmt = null;
        PreparedStatement updateUserStmt = null;
        try {
            conn = manager.getConnection();
            conn.setAutoCommit(false); // reczny commit
            updateCarStmt = conn.prepareStatement(RETURN_CAR_SQL);
            updateCarStmt.setString(1, plate);
            int changed1 =updateCarStmt.executeUpdate();
            updateUserStmt = conn.prepareStatement(RETURN_UPDATE_USER_SQL);
            updateUserStmt.setString(1, login);
            updateUserStmt.setString(2, plate);
            int changed2 =updateUserStmt.executeUpdate();
            if (changed1 > 0 && changed2 > 0) {
                System.out.println("returned");
                conn.commit();
            } else {
                System.out.println("not returned");
                conn.rollback(); // wycofuje zmiany
            }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { if (updateCarStmt != null) updateCarStmt.close(); } catch (Exception e) {};
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (Exception e) {};
            try { if (conn != null) conn.close(); } catch (Exception e) {};
        }
        return false;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        if(vehicle.getClass() == Motorcycle.class){
            String category = ((Motorcycle) vehicle).getCategory();
            try (Connection conn = manager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

                stmt.setString(1, vehicle.getBrand());
                stmt.setString(2, vehicle.getModel());
                stmt.setInt(3, vehicle.getYear());
                stmt.setDouble(4, vehicle.getPrice());
                stmt.setString(5, vehicle.getPlate());
                stmt.setString(6, category);

                int changed = stmt.executeUpdate();

                if (changed  > 0) {
                    System.out.println("Pojazd został pomyślnie dodany.");
                    return true;
                } else {
                    System.out.println("Nie udało się dodać pojazdu.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        else {
            try (Connection conn = manager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

                stmt.setString(1, vehicle.getBrand());
                stmt.setString(2, vehicle.getModel());
                stmt.setInt(3, vehicle.getYear());
                stmt.setDouble(4, vehicle.getPrice());
                stmt.setString(5, vehicle.getPlate());

                int changed = stmt.executeUpdate();

                if (changed > 0) {
                    System.out.println("Pojazd został pomyślnie dodany.");
                    return true;
                } else {
                    System.out.println("Nie udało się dodać pojazdu.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean removeVehicle(String plate) {
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_VEHICLE_SQL)) {

            stmt.setString(1, plate);

            int changed = stmt.executeUpdate();

            if (changed > 0) {
                System.out.println("Pojazd został pomyślnie usunięty.");
                return true;
            } else {
                System.out.println("Nie udało się usunąć pojazdu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Vehicle getVehicle(String vplate) {
        try  {
            Connection conn = manager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_VEHICLE_SQL);
            stmt.setString(1, vplate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String category = rs.getString("category");
                String plate = rs.getString("plate");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                boolean rent = rs.getBoolean("rent");
                if (category != null) {
                    return new Motorcycle(brand, model, year, price, plate, category);
                } else {
                    return new Car(brand, model, year, price, plate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Vehicle> getVehicles() {

        Collection<Vehicle> vehicles = new ArrayList<>();
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = manager.getConnection();
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(GET_ALL_VEHICLE_SQL);
            while(rs.next()){
                Vehicle v = null;
                String category = rs.getString("category");
                String plate = rs.getString("plate");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                boolean rent = rs.getBoolean("rent");
                if ( category!=null){
                    //Motorcycle(String brand, String model, int year, double price, String plate, String category)
                    v = new Motorcycle(brand,model,year,price,plate,category);

                }else{
                    v = new Car(brand,model,year,price,plate);
                }
                v.setRent(rent);
                vehicles.add(v);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if( rs!=null){try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
            if( connection!=null){try {connection.close();} catch (SQLException e) {e.printStackTrace();}}
        }
        return vehicles;

    }

}