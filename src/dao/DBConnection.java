package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static DBConnection uniqueInstance;
	private Connection connection = null; // the connection to the database

	private static final String DBNAME = "DMA-CSD-V252_10666009";
	private static final String SERVERNAME = "hildur.ucn.dk";
	private static final String PORTNUMBER = "1433";
	private static final String USERNAME = "DMA-CSD-V252_10666009";
	private static final String PASSWORD = "Password1!";

	private DBConnection() throws DataAccessException {
		String urlString = String.format(
				"jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true", SERVERNAME,
				PORTNUMBER, DBNAME);
		try {
			connection = DriverManager.getConnection(urlString, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new DataAccessException(String.format("Could not connect to database %s@%s:%s user %s", DBNAME,
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
