package interfaces;

import java.sql.SQLException;
import modules.Alert;

public interface AlertDBIF {
	Alert createAlert(Alert alert) throws SQLException;
}
