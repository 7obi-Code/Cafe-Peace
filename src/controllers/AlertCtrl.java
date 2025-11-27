package controllers;

import dao.AlertDB;
import interfaces.AlertDBIF;
import modules.Alert;
import modules.Product;

public class AlertCtrl {
	private AlertDBIF alertDB;
	
	
	public AlertCtrl() {
		alertDB = new AlertDB();
	}
	
	//Opretter en ny alert og gemmer den som aktiv
	public Alert createAlert(Alert.Type type, String description, Alert.Severity severity) throws SQLException {
	    Alert alert = new Alert(type, description, severity);
	    return alertDB.createAlert(alert); // save to DB
	}

	//Checker om produkt er ramt lavt stock, og kaster en advarsel når den er ramt minimum stock
	public boolean checkMinStock(Product p, int newQty) throws SQLException {
	    if (newQty <= p.getMinStock()) {
	        createAlert(Alert.Type.LOW_STOCK, 
	                    p.getName() + " er nået lavt lager.", 
	                    Alert.Severity.HØJ);
	        return true;
	    }
	    return false;
	}

	//Checker om produkt er ramt maximum stock, og kaster en advarsel hvis den er ramt maximum stock
	public boolean checkMaxStock(Product p, int newQty) throws SQLException {
		if (newQty >= p.getMaxStock()) {
			createAlert(Alert.Type.MAX_STOCK, 
					p.getName() + " er nået grænsen for lager.",
					Alert.Severity.LAV);
			return true;
		}
		return false;
	}
	
}
