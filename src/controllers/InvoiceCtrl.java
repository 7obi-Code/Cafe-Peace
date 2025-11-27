package controllers;
import interfaces.InvoiceDBIF;
import modules.Invoice;

public class InvoiceCtrl {
	private InvoiceDBIF invoiceDBIF;
	
	public Invoice findInvoiceByNo(int invoiceNo)	{
		invoiceDBIF.getInvoiceByNo(invoiceNo);
	}
}
