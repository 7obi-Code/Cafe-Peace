package controllers;

import modules.Invoice;
import modules.InvoiceLine;
import modules.Product;
import interfaces.ProductDBIF;
import dao.ProductDB;
import dao.DataAccessException;
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
	
	
	//Metoden til at hente invoices udfra fakturanummeret, det bliver gemt som nuværende invoice
	public Invoice insertInvoice(int invoiceNo)	throws DataAccessException {
		currentInvoice = invoiceCtrl.findInvoiceByNo(invoiceNo);
		return currentInvoice;
	}
	
	//Metoden til at bekræfte indleverings af produkter til lageret udfra currentinvoice //Bruger lambda/streams til at samle mængder pr produkt
	public void confirmDeposit(Map<Integer, Integer> countedQtyByProductId) throws Exception {
	    if (currentInvoice == null) {
	        throw new IllegalStateException("Der er ikke indlæst en faktura endnu.");
	    }
	    
	    // Use the quantities that the user entered
	    // countedQtyByProductId: Map<ProductId, OptaltAntal>
	    
	    Map<Product, Integer> newStockByProduct = productDB.updateStock(countedQtyByProductId);

	    // Check alerts for each product
	    newStockByProduct.forEach((product, newQty) -> {
	        try {
	            alertCtrl.checkMaxStock(product, newQty);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    });
	}

}


