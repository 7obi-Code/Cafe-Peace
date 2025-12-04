package interfaces;

import java.sql.SQLException;
import modules.Alert;
import modules.Product;

public interface AlertDBIF {
	Alert createAlert(Alert alert, Product product) throws SQLException;
}
