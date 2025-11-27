package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; 

public class DBConnection {

	private static DBConnection uniqueInstance; 	
	private Connection connection = null; // the connection to the database
	
	// The values of the constants should be read from a configuration file, etc.
	// You need to change the constants to the connection properties of your DB
	private static final String DBNAME = "InternshipManagement";
	private static final String SERVERNAME = "localhost";
	private static final String PORTNUMBER = "1433";
	private static final String USERNAME = "sa";
	private static final String PASSWORD = "secret2025*";

	// constructor - private because of singleton pattern
	private DBConnection() throws DataAccessException {
		String urlString = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false", SERVERNAME, PORTNUMBER,
				DBNAME);
		try {
			connection = DriverManager.getConnection(urlString, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new DataAccessException(String.format("Could not connect to database %s@%s:%d user %s", DBNAME,
					SERVERNAME, PORTNUMBER, USERNAME), e);
		}
	}

	public static DBConnection getInstance() throws DataAccessException {
		if (uniqueInstance == null) {
			uniqueInstance = new DBConnection();
		}
		return uniqueInstance;
	}

	public Connection getConnection() {
		return connection;
	}


}
