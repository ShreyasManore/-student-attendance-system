import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection.java
 * ------------------------
 * This class is responsible for creating and providing a single
 * JDBC connection object to the MySQL database. All other classes
 * (Student, Attendance, Login) use this class to talk to the database.
 *
 * Using a separate class for the connection follows good OOP practice
 * (Single Responsibility Principle) and avoids repeating connection
 * code everywhere.
 */
public class DatabaseConnection {

    // ---------------------------------------------------------
    // Database configuration constants
    // Change these values according to your local MySQL setup
    // ---------------------------------------------------------
    private static final String URL =
            "jdbc:mysql://localhost:3306/attendance_management_system";
    private static final String USER = "root";          // default MySQL username
    private static final String PASSWORD = "root";      // change this to your MySQL password

    // We keep a single shared connection (simple approach for a mini-project)
    private static Connection connection = null;

    /**
     * Returns a live connection to the database.
     * If a connection does not already exist, it creates one.
     *
     * @return Connection object, or null if connection fails
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load the MySQL JDBC driver (optional for newer JDBC versions,
                // but kept here for clarity and for older MySQL Connector/J versions)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Create the connection using URL, username, password
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Add mysql-connector-j to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     * Should be called when the application exits.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simple main method to test whether the database connection works.
     * Run this file directly to check your DB setup before running the
     * full application.
     */
    public static void main(String[] args) {
        Connection con = getConnection();
        if (con != null) {
            System.out.println("Connection test PASSED.");
        } else {
            System.out.println("Connection test FAILED.");
        }
        closeConnection();
    }
}
