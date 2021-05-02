package carsharing.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*class for db initialization*/
public class Storage {
    protected final String DB_URL;
    private static final java.lang.String JDBC_DRIVER = "org.h2.Driver";

    public Storage(String name) {
        DB_URL = "jdbc:h2:./src/carsharing/db/" + name;
        // register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public final void createTables() {
        // open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // all changes are saved automatically into the db
            conn.setAutoCommit(true);

            // creating the table with two columns
            String query = "CREATE TABLE IF NOT EXISTS company " +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL);";
            stmt.executeUpdate(query);

            // car table
            query = "CREATE TABLE IF NOT EXISTS car " +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL," +
                    "company_id INTEGER NOT NULL, " +
                    "CONSTRAINT fk_company FOREIGN KEY(company_id)" +
                    "REFERENCES company(id));";
            stmt.execute(query);

            // customer table
            query = "CREATE TABLE IF NOT EXISTS customer " +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL, " +
                    "rented_car_id INTEGER DEFAULT NULL, " +
                    "CONSTRAINT fk_car FOREIGN KEY(rented_car_id) " +
                    "REFERENCES car(id));";
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
