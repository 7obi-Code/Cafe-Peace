package dao;

import interfaces.AlertDBIF;
import modules.Alert;
import modules.Product;

import java.sql.*;
import java.time.LocalDateTime;


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
}
