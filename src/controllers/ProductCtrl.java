package controllers;

import modules.Alert;
import modules.Invoice;
import modules.InvoiceLine;
import modules.Product;
import interfaces.ProductDBIF;
import dao.ProductDB;
import dao.DataAccessException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ProductCtrl {
	private final InvoiceCtrl invoiceCtrl;
	private final AlertCtrl alertCtrl;
	private final ProductDBIF productDB;
	private Invoice currentInvoice; //Den nuværende invoice som er loaded
	
	public ProductCtrl() throws DataAccessException {
		this.invoiceCtrl = new InvoiceCtrl();
		this.alertCtrl = new AlertCtrl();
		this.productDB = new ProductDB();
	}
	
	public Product findProductById(int productId) throws DataAccessException	{
		try	{
			return productDB.findProductById(productId, true);
		} catch (SQLException e) {
    		throw new DataAccessException("Could not find product by Id", e);
    	}
	}
	
	
	//Metoden til at hente invoices udfra fakturanummeret, det bliver gemt som nuværende invoice
	public Invoice insertInvoice(int invoiceNo)	throws DataAccessException {
		currentInvoice = invoiceCtrl.findInvoiceByNo(invoiceNo);
		return currentInvoice;
	}
	
	//Metoden til at bekræfte indleverings af produkter til lageret udfra currentinvoice //Bruger lambda/streams til at samle mængder pr produkt
	public void confirmDeposit(HashMap<Integer, Integer> countedQtyByProductId) throws Exception {
	    if (currentInvoice == null) {
	        throw new IllegalStateException("Der er ikke indlæst en faktura endnu.");
	    }

	    productDB.updateStock(countedQtyByProductId);
	    
	    alertCtrl.checkDepositMatchInvoice(currentInvoice, countedQtyByProductId);
	    
	    for (int productId : countedQtyByProductId.keySet()) {
	        Product p = productDB.findProductById(productId, true);
	        alertCtrl.checkMaxStock(p);
	    }
	}
	
	public List<Alert> getRecentAlerts() throws DataAccessException 	{
		return alertCtrl.getRecentAlerts();
	}
}


