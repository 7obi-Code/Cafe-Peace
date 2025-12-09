package interfaces;

import dao.DataAccessException;
import modules.Invoice;
import modules.Invoice.Status;

public interface InvoiceDBIF {
	Invoice getInvoiceByNo(int invoiceNo, boolean fullAssociation) throws DataAccessException;

	void updateInvoiceStatus(int invoiceNo, Status newStatus) throws DataAccessException;
}
