package interfaces;

import java.sql.SQLException;
import dao.DataAccessException;
import modules.Stock;
import java.time.LocalDateTime;

public interface StockDBIF {

	// Finder den nyeste stock-record for produktet
	Stock findStockByProductId(int productId) throws SQLException, DataAccessException;

	// Opretter en ny stock-record (fx ved ny deposit)
	void createStock(int productId, int amount, LocalDateTime timestamp) throws SQLException, DataAccessException;

}
