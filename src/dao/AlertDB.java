package dao;

import interfaces.AlertDBIF;
import modules.Alert;
import java.sql.*;


public class AlertDB implements AlertDBIF {
	
	private static final String INSERT =
			"INSERT INTO Alerts (type, description, severity, timestamp) VALUES (?, ?, ?, ?)";
	
	@Override
	public Alert createAlert(Alert alert) throws SQLException {
		
		Connection conn; try {
		    conn = DBConnection.getInstance().getConnection();
		} catch (DataAccessException e) {
		    throw new SQLException("Could not get DB connection", e);
		}
	
        PreparedStatement ps = conn.prepareStatement(INSERT);
        ps.setString(1, alert.getType().name());
        ps.setString(2, alert.getDescription());
        ps.setString(3, alert.getSeverity().name());
        ps.setTimestamp(4, Timestamp.valueOf(alert.getTimestamp()));
        ps.executeUpdate();
        return alert;
	}
}
