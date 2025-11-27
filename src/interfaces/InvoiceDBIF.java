package interfaces;
import dao.DataAccessException;
import modules.Invoice;

public interface InvoiceDBIF {
	Invoice getInvoiceByNo(int invoiceNo, boolean fullAssociation) throws DataAccessException;
}
