package controllers;
import dao.DataAccessException;
import dao.InvoiceDB;
import interfaces.InvoiceDBIF;
import modules.Invoice;
import modules.Invoice.Status;

public class InvoiceCtrl {
	private InvoiceDBIF invoiceDBIF;
	private Status status;
	
	public InvoiceCtrl() throws DataAccessException {
        this.invoiceDBIF = new InvoiceDB();
    }
	
	public Invoice findInvoiceByNo(int invoiceNo) throws DataAccessException	{
		return invoiceDBIF.getInvoiceByNo(invoiceNo, true);
	}
	
	public Status checkStatusByNo(int invoiceNo) throws DataAccessException	{
		Invoice i = findInvoiceByNo(invoiceNo);
		return i.getStatus();
	}
	
	public void updateInvoiceStatus(int invoiceNo, Status newStatus) throws DataAccessException	{
		invoiceDBIF.updateInvoiceStatus(invoiceNo, newStatus);
	}
}

