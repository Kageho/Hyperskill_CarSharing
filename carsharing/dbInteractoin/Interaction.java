package carsharing.dbInteractoin;

import java.sql.*;

public class Interaction {
    private static final java.lang.String JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL;
    private final static String selectQuery = "SELECT name FROM company ORDER BY ID;";

    public Interaction(String[] args) {
        String name = "someName";
        if (args.length > 1) {
            name = args[1];
        }
        DB_URL = "jdbc:h2:./src/carsharing/db/" + name;
        // register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // creating tables with one to many relationships
        createCompanyTable();
        createCarTable();
    }

    public boolean getCompanies() {
        boolean isEmpty = true;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // opening a connection

            // preparing a query
            ResultSet result = stmt.executeQuery(selectQuery);
            // print the result
            isEmpty = !printCompanies(result);
            // cleaning the environment
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isEmpty;
    }

    // method returns true if at least one company was printed
    private boolean printCompanies(ResultSet rs) throws SQLException {
        boolean result = false;
        if (rs == null) {
            System.out.println("smth went wrong, try again later");
        } else if (!rs.isBeforeFirst()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose the company:");
            int inedx = 1;
            while (rs.next()) {
                System.out.printf("%d. %s\n", inedx++, rs.getString(1));
            }
            System.out.println("0. Back");
            result = true;
        }
        return result;
    }

    // foreign key is a car company's id
    public void getCars(int foreignKey) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT name FROM car WHERE company_id = " + foreignKey +
                    " ORDER BY id;";
            ResultSet resultSet = stmt.executeQuery(query);
            // calling a method for printing cars
            printCars(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void printCars(ResultSet rs) throws SQLException {
        if (rs == null) {
            System.out.println("Smth went wrong, try again later");
        } else if (!rs.isBeforeFirst()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            int index = 1;
            while (rs.next()) {
                System.out.printf("%d. %s\n", index++, rs.getString("name"));
            }
        }
    }

    public void insertCompany(String name) {
        // opening a connection
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();) {
            // changes will be saved automatically
            conn.setAutoCommit(true);

            // preparing a query
            StringBuilder query = new StringBuilder("INSERT INTO company (name)");
            query.append(" VALUES ('");
            query.append(name);
            query.append("');");

            stmt.execute(query.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isCompanyPresent(int id) {
        boolean result = false;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT name FROM company WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(query);
            // set is not empty
            if (rs.isBeforeFirst()) {
                result = true;
                rs.next();
                System.out.printf("'%s' company\n", rs.getString(1));

            } else {
                System.out.println("There is no such company!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    // foreignKey is a company's id
    public void insertCar(String name, int foreignKey) {
        // opening a connection
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);

            String query = "INSERT INTO car (name, company_id)" +
                    "VALUES ('" + name + "', " + foreignKey + ");";

            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createCompanyTable() {
        // open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // all changes are saved automatically into the db
            conn.setAutoCommit(true);
            // dropping the table
            // It is commented to pass tests
            stmt.execute("DROP TABLE IF EXISTS car");
            stmt.execute("DROP TABLE IF EXISTS company");
            // creating the table with two columns
            String query = "CREATE TABLE company" +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL)";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCarTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // changes will be saved
            conn.setAutoCommit(true);
            //  stmt.execute("DROP TABLE car");

            String query = "CREATE TABLE car" +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL," +
                    "company_id INT NOT NULL," +
                    "CONSTRAINT fk_company FOREIGN KEY(company_id)" +
                    "REFERENCES company(id));";
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
