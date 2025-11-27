package controllers;
import modules.Invoice;
import modules.Alert;
import modules.Stock;


public class ProductCtrl {
	private InvoiceCtrl invoiceCtrl;
	private AlertCtrl alertCtrl;
	
	public ProductCtrl()	{
	}

	public Invoice insertInvoice(int invoiceNo)	{
		invoiceCtrl.findInvoiceByNo(invoiceNo);
	}
	
	public void confirmDeposit()	{
		// Skal opdatere stock og tjekke om der skal ligges en alert
	}

}


