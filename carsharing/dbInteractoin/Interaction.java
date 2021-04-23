package carsharing.dbInteractoin;

import java.sql.*;

public class Interaction {
    private static final java.lang.String JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL;
    private final static String selectQuery = "SELECT * FROM company ORDER BY id";

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
    }

    public void getCompanies() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // opening a connection
            // changes will be saved automatically
            conn.setAutoCommit(true);

            // preparing a query
            ResultSet result = stmt.executeQuery(selectQuery);
            // print the result
            printCompanies(result);
            // cleaning the environment
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void printCompanies(ResultSet rs) throws SQLException {
        if (rs == null) {
            System.out.println("smth went wrong, try again later");
        } else if (!rs.isBeforeFirst()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Company list:");
            while (rs.next()) {
                System.out.printf("%d. %s\n", rs.getInt(1), rs.getString(2));
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

    public void createDB() {
        // open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // all changes are saved automatically into the db
            conn.setAutoCommit(true);
            // dropping the table
            stmt.execute("DROP TABLE company");
            // creating the table with two columns
            String query = "CREATE TABLE company" +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL)";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
