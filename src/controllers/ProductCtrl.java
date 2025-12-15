package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.DataAccessException;
import dao.ProductDB;
import interfaces.ProductDBIF;
import modules.Alert;
import modules.Invoice;
import modules.Product;

public class ProductCtrl {
	private final InvoiceCtrl invoiceCtrl;
	private final AlertCtrl alertCtrl;
	private final ProductDBIF productDB;
	private Invoice currentInvoice; // Den nuværende invoice som er loaded

	public ProductCtrl() throws DataAccessException {
		this.invoiceCtrl = new InvoiceCtrl();
		this.alertCtrl = new AlertCtrl();
		this.productDB = new ProductDB();
	}

	public Product findProductById(int productId) throws DataAccessException {
		try {
			return productDB.findProductById(productId, true);
		} catch (SQLException e) {
			throw new DataAccessException("Could not find product by Id", e);
		}
	}

	// Metoden til at hente invoices udfra fakturanummeret, det bliver gemt som
	// nuværende invoice
	public Invoice insertInvoice(int invoiceNo) throws DataAccessException {
		currentInvoice = invoiceCtrl.findInvoiceByNo(invoiceNo);
		return currentInvoice;
	}

	// Metoden til at bekræfte indleverings af produkter til lageret udfra
	// currentinvoice //Bruger lambda/streams til at samle mængder pr produkt
	public void confirmDeposit(HashMap<Integer, Integer> countedQtyByProductId) throws Exception {
		if (currentInvoice == null) {
			throw new IllegalStateException("Der er ikke indlæst en faktura endnu.");
		}

		productDB.updateStockDeposit(countedQtyByProductId);

		alertCtrl.checkDepositMatchInvoice(currentInvoice, countedQtyByProductId);

		for (int productId : countedQtyByProductId.keySet()) {
			Product p = productDB.findProductById(productId, true);
			alertCtrl.checkMaxStock(p);
		}
	}

	public void confirmWithdraw(Product product, int withdrawQty) throws Exception {
		if (product == null)	{
			throw new IllegalStateException("Der er ikke indlæst et produkt endnu.");
		}
		
		productDB.updateStockWithdraw(product, withdrawQty) ;
		
		Product p = productDB.findProductById(product.getProductId(), true);
		alertCtrl.checkMinStock(p);
	}

	public ArrayList<Product> getAllProducts() throws DataAccessException {
		return productDB.getAllProducts();
	}

	public List<Alert> getRecentAlerts() throws DataAccessException {
		return alertCtrl.getRecentAlerts();
	}
}
