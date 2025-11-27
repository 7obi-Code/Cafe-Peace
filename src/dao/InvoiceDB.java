package dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

import db.DataAccessException;
import interfaces.InvoiceDBIF;
import model.Employee;
import modules.Invoice;

public class InvoiceDB implements InvoiceDBIF {
	private ProductDB productDB;
	
	private static final String SELECT_ALL_INVOICES = 
			"SELECT invoiceId, supPhone_FK, alert_FK, staff_FK FROM Invoice";
	
	private static final String SELECT_BY_INVOICENO = SELECT_ALL_INVOICES + " WHERE invoiceId = ?";
	
	private PreparedStatement selectByInvoiceNo;
	
	
	public InvoiceDB() throws DataAccessException {
		productDB = new ProductDB();
		try {
			selectByInvoiceNo = DBConnection.getInstance().getConnection().prepareStatement(SELECT_BY_INVOICENO);
		} catch (SQLException e) {
			throw new DataAccessException("Could not prepare statement",e);
		}
	}
	
	public Invoice getInvoiceByNo(int invoiceNo, boolean fullAssociation)	{
		try {
			selectByInvoiceNo.setInt(1, invoiceNo);
			ResultSet rs = selectByInvoiceNo.executeQuery();
			Invoice e = buildObject(rs, fullAssociation);
			return e;
		} catch (SQLException e) {
			throw new DataAccessException("Could not bind param or select employee by ID", e);
		}
	}
	
	private Invoice buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		Invoice i = null;
		try {
			if(rs.next()) {
				i = new Invoice(
						rs.getInt("invoiceNo")
						);
				if(fullAssociation) {
					
					
					i.setInvoiceLines(null);
					//List<Vehicle> vehicles = vehicleDao.findByEmployeeId(e.getId());
					//e.setVehicles(vehicles);
				}
			}
		} catch (SQLException e1) {
			throw new DataAccessException("Could not read result set for invoice", e1);
		}
		return i;
	}
	
	private ArrayList<InvoiceLine> buildInvoiceLines() {
		
	}
}
