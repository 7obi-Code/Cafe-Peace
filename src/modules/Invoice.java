package modules;

import java.util.ArrayList;

public class Invoice {
	
	private int invoiceNo;
	private ArrayList<InvoiceLine> invoiceLines;
	
	public Invoice(int invoiceNo)	{
		this.invoiceNo = invoiceNo;
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
