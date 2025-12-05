package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

import interfaces.InvoiceDBIF;
import modules.Product;
import modules.Invoice;
import modules.Invoice.Status;
import modules.InvoiceLine;

public class InvoiceDB implements InvoiceDBIF {
	private ProductDB productDB;
	
	private static final String SELECT_ALL_INVOICES = 
			"SELECT invoiceNo, supPhone_FK, status, alert_FK, staff_FK FROM Invoice";
	
	private static final String SELECT_BY_INVOICENO = SELECT_ALL_INVOICES + " WHERE invoiceNo = ?";
	
	private static final String SELECT_ALL_INVOICELINES =
			"SELECT quantity, invoice_FK, product_FK FROM InvoiceLine";
	
	private static final String SELECT_INVOICELINE_BY_INVOICENO = SELECT_ALL_INVOICELINES + " WHERE invoice_FK = ?";
	
	private static final String UPDATE_INVOICE_STATUS = "UPDATE Invoice SET status = ? WHERE invoiceNo = ?";
	
	private PreparedStatement selectByInvoiceNo;
	private PreparedStatement selectInvoiceLinesByInvoiceNo;
	private PreparedStatement updateInvoiceStatus;
	
	public InvoiceDB() throws DataAccessException {
		productDB = new ProductDB();
		try {
			selectByInvoiceNo = DBConnection.getInstance().getConnection().prepareStatement(SELECT_BY_INVOICENO);
			selectInvoiceLinesByInvoiceNo = DBConnection.getInstance().getConnection().prepareStatement(SELECT_INVOICELINE_BY_INVOICENO);
			updateInvoiceStatus = DBConnection.getInstance().getConnection().prepareStatement(UPDATE_INVOICE_STATUS);
		} catch (SQLException e) {
			throw new DataAccessException("Could not prepare statements",e);
		}
	}
	
	public Invoice getInvoiceByNo(int invoiceNo, boolean fullAssociation) throws DataAccessException {
		try {
			selectByInvoiceNo.setInt(1, invoiceNo);
			ResultSet rs = selectByInvoiceNo.executeQuery();
			Invoice i = buildObject(rs, fullAssociation);
			return i;
		} catch (SQLException e) {
			throw new DataAccessException("Could not find param or select invoice by No", e);
		}
	}
	
	public void updateInvoiceStatus(int invoiceNo, Status newStatus) throws DataAccessException {
	    try {
	        updateInvoiceStatus.setString(1, newStatus.toString()); // Konverter enum til string
	        updateInvoiceStatus.setInt(2, invoiceNo);
	        updateInvoiceStatus.executeUpdate();
	    } catch (SQLException e) {
	        throw new DataAccessException("Could not update invoice status", e);
	    }
	}
	
	
	private Invoice buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		Invoice i = null;
		try {
			if(rs.next()) {
				i = new Invoice(
						rs.getInt("invoiceNo"),
						Status.valueOf(rs.getString("status"))
						);
				if(fullAssociation) {
					ArrayList<InvoiceLine> il = buildInvoiceLines(rs.getInt("invoiceNo"));
					i.setInvoiceLines(il);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not read result set for invoice", e);
		}
		return i;
	}
	
	private ArrayList<InvoiceLine> buildInvoiceLines(int invoiceNo) throws DataAccessException {
		ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
		try	{
			selectInvoiceLinesByInvoiceNo.setInt(1, invoiceNo);
			ResultSet rs = selectInvoiceLinesByInvoiceNo.executeQuery();
			
			while (rs.next())	{
				Product product = productDB.findProductById(rs.getInt("product_FK"), true);
				InvoiceLine il = new InvoiceLine(
				rs.getInt("quantity"), product);
			invoiceLines.add(il);
			}
			
		} catch (SQLException e) {
			throw new DataAccessException("Could not read invoicelines", e);
		}
		return invoiceLines;
	}
}
