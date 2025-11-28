package controllers;
import dao.DataAccessException;
import interfaces.InvoiceDBIF;
import modules.Invoice;

public class InvoiceCtrl {
	private InvoiceDBIF invoiceDBIF;
	
	public Invoice findInvoiceByNo(int invoiceNo)	{
		return invoiceDBIF.getInvoiceByNo(invoiceNo, true) throws DataAccessException;
	}
}
