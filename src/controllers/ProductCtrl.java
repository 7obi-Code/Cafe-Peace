package controllers;
import modules.Invoice;
import modules.Product;
import modules.Alert;
import modules.Stock;
import interfaces.ProductDBIF;


public class ProductCtrl {
	private InvoiceCtrl invoiceCtrl;
	private AlertCtrl alertCtrl;
	private ProductDBIF productDBIF;
	

	public Invoice insertInvoice(int invoiceNo)	{
		invoiceCtrl.findInvoiceByNo(invoiceNo);
	}
	
	public void confirmDeposit()	{
		// Skal opdatere stock og tjekke om der skal (ligges en alert = metoden hedder checkMaxStock(Product p, int newQty))
	}

}


