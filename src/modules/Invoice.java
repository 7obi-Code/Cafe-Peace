package modules;

import java.util.ArrayList;

public class Invoice {
	
	private int invoiceNo;
	private Status status;
	private ArrayList<InvoiceLine> invoiceLines;
	
	public enum Status	{
		PENDING, COMPLETED
	}

	public Invoice(int invoiceNo, Status status)	{
		this.invoiceNo = invoiceNo;
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}	
	
	public int getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(int invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public ArrayList<InvoiceLine> getInvoiceLines() {
		return invoiceLines;
	}

	public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
		this.invoiceLines = invoiceLines;
	}
	
}
