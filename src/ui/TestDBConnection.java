package ui;

import java.sql.Connection;
import java.sql.SQLException;

import dao.DBConnection;
import dao.DataAccessException;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            // Get the singleton instance of DBConnection
            DBConnection db = DBConnection.getInstance();
            Connection conn = db.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed: Connection is null or closed.");
            }
        } catch (DataAccessException e) {
            System.out.println("DataAccessException: Could not connect to database.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException while checking connection.");
            e.printStackTrace();
        }
    }
}
