package carsharing.company;

import carsharing.storage.Storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl extends Storage implements CompanyDao {
    private final static String SELECT_ALL = "SELECT name FROM company ORDER BY ID;";

    public CompanyDaoImpl(String name) {
        super(name);
    }


    @Override
    public List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            ResultSet rs = stmt.executeQuery(SELECT_ALL);

            while (rs.next()) {
                companies.add(new Company(rs.getString(1)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return companies;
    }

    @Override
    public void createCompany(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String query = "INSERT INTO company (name) VALUES ('" + name + "');";
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
// костыль для прохождения теста на hyperskill
    @Override
    public int getCompanyId(String name) {
        int id = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT id FROM company WHERE name = '" + name + "';";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int buf = rs.getInt(1);
                if (buf != 0) {
                    id = buf;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }
}
