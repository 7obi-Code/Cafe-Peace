package interfaces;

import modules.Alert;

public interface AlertDBIF {
	Alert createAlert(Alert alert) throws SQLException;
}
