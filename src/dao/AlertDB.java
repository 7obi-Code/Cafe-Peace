package dao;

import interfaces.AlertDBIF;
import modules.Alert;
import modules.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class AlertDB implements AlertDBIF {
	
	private static final String INSERT =
			"INSERT INTO Alert (type, description, severity, timestamp, productId_FK) VALUES (?, ?, ?, ?, ?)";
	
	@Override
	public Alert createAlert(Alert alert, Product product) throws SQLException {
		
		Connection conn; try {
		    conn = DBConnection.getInstance().getConnection();
		} catch (DataAccessException e) {
		    throw new SQLException("Could not get DB connection", e);
		}
		
		Product p = product;
        PreparedStatement ps = conn.prepareStatement(INSERT);
        ps.setString(1, alert.getType().name());
        ps.setString(2, alert.getDescription());
        ps.setString(3, alert.getSeverity().name());
        ps.setObject(4, LocalDateTime.now());
        ps.setInt(5, p.getProductId());
        ps.executeUpdate();
        return alert;
	}
	
	
	//Henter alerts fra databasen. Sender til UI.
	private static final String SELECT_ALL =
	        "SELECT type, description, severity, timestamp FROM Alert"; // ingen ORDER BY tid

	public List<Alert> getRecentAlerts() throws SQLException {
	    Connection conn;
	    try {
	        conn = DBConnection.getInstance().getConnection();
	    } catch (DataAccessException e) {
	        throw new SQLException("Could not get DB connection", e);
	    }

	    List<Alert> alerts = new ArrayList<>();

	    try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Alert.Type type = Alert.Type.valueOf(rs.getString("type"));
	            String description = rs.getString("description");
	            Alert.Severity severity = Alert.Severity.valueOf(rs.getString("severity"));
	            // vi bruger stadig timestamp hvis du vil vise det senere, men vi checker ikke “hvornår”
	            LocalDateTime timestamp = rs.getObject("timestamp", LocalDateTime.class);

	            alerts.add(new Alert(type, description, severity, timestamp));
	        }
	    }
	    return alerts;
	}
	
	
	
}
