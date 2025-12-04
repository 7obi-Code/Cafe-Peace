package controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import dao.AlertDB;
import dao.DataAccessException;
import interfaces.AlertDBIF;
import modules.Alert;
import modules.Product;

public class AlertCtrl {
	private AlertDBIF alertDB;
	
	
	public AlertCtrl() throws DataAccessException {
		alertDB = new AlertDB();
	}
	
	//Opretter en ny alert og gemmer den som aktiv
	public Alert createAlert(Alert.Type type, String description, Alert.Severity severity, LocalDateTime timestamp, Product product) throws SQLException {
	    Alert alert = new Alert(type, description, severity, timestamp);
	    return alertDB.createAlert(alert, product); // save to DB
	}

	//Checker om produkt er ramt lavt stock, og kaster en advarsel når den er ramt minimum stock
	
	/*/public boolean checkMinStock(Product p, int newQty) throws SQLException {
	    if (newQty <= p.getMinStock()) {
	        createAlert(Alert.Type.LOW_STOCK, 
	                    p.getName() + " er nået lavt lager.", 
	                    Alert.Severity.HØJ,
	                    LocalDateTime.now(), product);
	        return true;
	    }
	    return false;
	} /*/

	//Checker om produkt er ramt maximum stock, og kaster en advarsel hvis den er ramt maximum stock
	public boolean checkMaxStock(Product p) throws DataAccessException {
		try	{
			Product product = p;
			if (product.getStock().getAmount() > product.getMaxStock()) {
			createAlert(Alert.Type.MAX_STOCK, 
					product.getName() + " er nået grænsen for lager.",
					Alert.Severity.LAV,
					LocalDateTime.now(), product);
				return true;
				}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Could not check Max Stock", e);
		}
	}
}
