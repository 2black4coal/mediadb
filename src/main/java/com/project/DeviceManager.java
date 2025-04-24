
package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DeviceManager {
    private static final String URL = "jdbc:postgresql://localhost:5433/mediadb"; // Ensure the DB name is correct
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        try {
            // Load the PostgreSQL driver class (optional for newer versions of JDBC)
            Class.forName("org.postgresql.Driver");

            // Try to establish the connection
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;  // Returning null if connection failed, optional: throw an exception instead
    }
}
