package controllers;
import interfaces.InvoiceDBIF;
import modules.Invoice;

public class InvoiceCtrl {
	private InvoiceDBIF invoiceDPIF;
	
	public Invoice findInvoiceByNo(int invoiceNo)	{
		invoiceDPIF.getInvoiceByNo(invoiceNo);
	}
}
