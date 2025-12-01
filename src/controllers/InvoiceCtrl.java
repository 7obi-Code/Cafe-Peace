package controllers;
import dao.DataAccessException;
import interfaces.InvoiceDBIF;
import modules.Invoice;

public class InvoiceCtrl {
	private InvoiceDBIF invoiceDBIF;
	
	public Invoice findInvoiceByNo(int invoiceNo) throws DataAccessException	{
		return invoiceDBIF.getInvoiceByNo(invoiceNo, true);
	}
}
