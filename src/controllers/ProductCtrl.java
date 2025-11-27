package controllers;

import modules.Invoice;
import modules.InvoiceLine;
import modules.Product;
import interfaces.ProductDBIF;
import dao.ProductDB;

import java.util.Map;
import java.util.stream.Collectors;


public class ProductCtrl {
	private final InvoiceCtrl invoiceCtrl;
	private final AlertCtrl alertCtrl;
	private final ProductDBIF productDB;
	private Invoice currentInvoice; //Den nuværende invoice som er loaded
	
	public ProductCtrl() {
		this.invoiceCtrl = new InvoiceCtrl();
		this.alertCtrl = new AlertCtrl();
		this.productDB = new ProductDB();
	}
	
	
	//Metoden til at hente invoices udfra fakturanummeret, det bliver gemt som nuværende invoice
	public Invoice insertInvoice(int invoiceNo)	throws Exception {
		currentInvoice = invoiceCtrl.findInvoiceByNo(invoiceNo);
		return currentInvoice;
	}
	
	//Metoden til at bekræfte indleverings af produkter til lageret udfra currentinvoice //Bruger lambda/streams til at samle mængder pr produkt
	public void confirmDeposit() throws Exception {
		if (currentInvoice == null) {
			throw new IllegalStateException("Der er ikke indlæst en faktura endnu.");
		}
		
		//Lambda/Stream samler total antal per produktId
		Map<Integer, Integer> qtyByProductId =
				currentInvoice.getInvoiceLines()
					.stream()
					.collect(Collectors.toMap(
							line -> line.getProduct().getProductId(),
							InvoiceLine::getQuantity,
							Integer::sum
					));
		//Lageret opdateres i databasen, og de nye lagerantal retuneres
		Map<Product, Integer> newStockByProduct = productDB.updateStock(qtyByProductId);
		
		//Tjekker alerts for hver vare (Bruger forEach med lambda)
		newStockByProduct.forEach((product, newQty) -> {
			try {
				alertCtrl.checkMaxStock(product, newQty);
			} catch (Exception e) {
				//Simpel wrap i runtime
				throw new RuntimeException(e);
			}
			
		});
		
	}

}


