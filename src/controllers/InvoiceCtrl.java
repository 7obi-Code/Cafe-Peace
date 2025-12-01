package controllers;
import dao.DataAccessException;
import dao.InvoiceDB;
import interfaces.InvoiceDBIF;
import modules.Invoice;

public class InvoiceCtrl {
	private InvoiceDBIF invoiceDBIF;
	
	public InvoiceCtrl() throws DataAccessException {
        this.invoiceDBIF = new InvoiceDB();
    }
	
	public Invoice findInvoiceByNo(int invoiceNo) throws DataAccessException	{
		return invoiceDBIF.getInvoiceByNo(invoiceNo, true);
	}
}
