package modules;
import java.sql.SQLException;
import java.time.LocalDateTime;

import dao.AlertDB;
import dao.DataAccessException;
import dao.ProductDB;
import interfaces.AlertDBIF;
import interfaces.ProductDBIF;

public class StockService {
	private final ProductDBIF productDB;
	private final AlertDBIF alertDB;
	
	public StockService() throws DataAccessException {
		productDB = new ProductDB();
		alertDB = new AlertDB();
	}
	
	public synchronized void Withdraw(int productId, int quantity, String username) throws SQLException, DataAccessException {
		Product product = productDB.findProductById(productId, true);
		
		if (product == null) 
			throw new IllegalStateException("Der er ikke indlæst et produkt endnu.");
		
		if (product.getStock().getAmount() >= quantity) {
			productDB.updateStockWithdraw(product, quantity);
			System.out.println(username + " withdrew " + quantity + "of product with id: " + productId + ".");
		}
		else {
			System.out.println(username + " could not withdraw " + quantity + "of productId: " + productId);
			alertDB.createAlert(new Alert(Alert.Type.LOW_STOCK, "Der er ikke nok varer på lager.", Alert.Severity.HØJ, LocalDateTime.now()), product);
		}
		
	}
}
