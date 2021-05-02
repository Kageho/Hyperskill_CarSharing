package carsharing.car;

import carsharing.storage.Storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl extends Storage implements CarDao {
    public CarDaoImpl(String name) {
        super(name);
    }

    // method returns all cars that have been made by company with companyId
    @Override
    public List<Car> getCars(int companyId) {
        List<Car> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            String query = "SELECT name from car WHERE company_id = " + companyId;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                result.add(new Car(rs.getString(1)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    // a new car is created by a company with companyId
    @Override
    public void createCar(int companyId, String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String query = "INSERT INTO car (name, company_id) VALUES ('" + name + "', " + companyId + ");";
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
