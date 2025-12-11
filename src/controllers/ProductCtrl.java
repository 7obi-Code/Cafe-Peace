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
import modules.StockService;

public class ProductCtrl {
	private final StockService stockService;
	private final InvoiceCtrl invoiceCtrl;
	private final AlertCtrl alertCtrl;
	private final ProductDBIF productDB;
	private Invoice currentInvoice; // Den nuværende invoice som er loaded

	public ProductCtrl() throws DataAccessException {
		this.invoiceCtrl = new InvoiceCtrl();
		this.alertCtrl = new AlertCtrl();
		this.productDB = new ProductDB();
		this.stockService = new StockService();
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

	public void confirmWithdraw(int productId, int withdrawQty, String username) throws Exception {
		stockService.Withdraw(productId, withdrawQty, username);
	}

	public void confirmWithdraw(Product product, int withdrawQty) throws Exception {
		stockService.Withdraw(product.getProductId(), withdrawQty, "Username"); // TODO: Det her er ikke smukt.
	}

	public ArrayList<Product> getAllProducts() throws DataAccessException {
		return productDB.getAllProducts();
	}

	public List<Alert> getRecentAlerts() throws DataAccessException {
		return alertCtrl.getRecentAlerts();
	}
}
