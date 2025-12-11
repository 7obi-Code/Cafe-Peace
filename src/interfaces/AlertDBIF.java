package interfaces;

import java.sql.SQLException;
import java.util.List;

import modules.Alert;
import modules.Product;

public interface AlertDBIF {
	Alert createAlert(Alert alert, Product product) throws SQLException;

	// Alert lsit til UI.
	List<Alert> getRecentAlerts() throws SQLException;
}
