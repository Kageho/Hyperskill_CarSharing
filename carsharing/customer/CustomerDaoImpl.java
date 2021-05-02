package carsharing.customer;

import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.storage.Storage;

import java.sql.*;
import java.util.*;

public class CustomerDaoImpl extends Storage implements CustomerDao {
    private static final String SELECT_NAMES = "SELECT name from customer ORDER BY ID;";

    public CustomerDaoImpl(String name) {
        super(name);
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            ResultSet rs = stmt.executeQuery(SELECT_NAMES);
            while (rs.next()) {
                result.add(new Customer(rs.getString(1)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    // returns customer or null if there is no customer with customerId
    @Override
    public Customer getInfoAboutRentedCar(int customerId) {
        Customer customer = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String query = "SELECT name, rented_car_id FROM customer WHERE id = " + customerId;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                customer = new Customer(rs.getString(1));
                int carId = rs.getInt(2);
                if (carId != 0) {
                    //
                    query = "SELECT name, company_id FROM car WHERE id = " + carId;
                    rs = stmt.executeQuery(query);

                    int companyId = 0;
                    // set info about car
                    while (rs.next()) {
                        customer.setCar(new Car(rs.getString(1)));
                        customer.setHasCar(true);
                        // set info about company
                        companyId = rs.getInt(2);
                    }

                    query = "SELECT name FROM company WHERE id = " + companyId;
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        customer.setCompany(new Company(rs.getString(1)));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer;
    }

    // returns rented car by setting null to specific row with index that equals to customerId
    // and column named rented_car_id
    @Override
    public void returnCar(int customerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String query = "UPDATE customer SET rented_car_id = NULL WHERE id = " + customerId;
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
   // it finds id of car by its name in car table
   // then the id is written in customer table in rented_car_id column and row with
   // customer id
    @Override
    public void rentCar(String carName, int customerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            String query = "SELECT id FROM car WHERE name = '" + carName + "'";
            ResultSet rs = stmt.executeQuery(query);
            int carId = -1;
            while (rs.next()) {
                carId = rs.getInt(1);
            }


            query = "UPDATE customer SET rented_car_id = " + carId + " WHERE id = " + customerId;
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // it simply adds new new customer into customer table
    @Override
    public void createCustomer(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String query = "INSERT INTO customer (name) VALUES ('" + name + "')";
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
// returns list of not rented cars of the company
    @Override
    public List<Car> getAvailableCars(Company company) {
        List<Car> cars = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            int company_id = -1;
            String queryId = "SELECT id FROM company WHERE name = '" + company.getName() + "'";
            ResultSet resultSetId = stmt.executeQuery(queryId);
            while (resultSetId.next()) {
                company_id = resultSetId.getInt(1);
            }

            String rentedCarQuery = "SELECT rented_car_id FROM customer WHERE rented_car_id IS NOT NULL";
            ResultSet rsRentedCars = stmt.executeQuery(rentedCarQuery);

            Set<Integer> rentedCars = new HashSet<>();
            while (rsRentedCars.next()) {
                rentedCars.add(rsRentedCars.getInt(1));
            }

            String companyCarsQuery = "SELECT id, name FROM car WHERE company_id = " + company_id + " ORDER BY id;";
            ResultSet rsAllCompanyCars = stmt.executeQuery(companyCarsQuery);

            while (rsAllCompanyCars.next()) {
                int id = rsAllCompanyCars.getInt(1);
                String carName = rsAllCompanyCars.getString(2);
                if (!rentedCars.contains(id)) {
                    cars.add(new Car(carName));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cars;
    }
}
