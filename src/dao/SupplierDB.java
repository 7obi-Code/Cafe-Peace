package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import interfaces.SupplierDBIF;
import modules.Product;
import modules.Supplier;

public class SupplierDB implements SupplierDBIF {
	
    private static final String SELECT_ALL_SUPPLIERS =
    		"SELECT phone, name, addressId_FK, email, cvr FROM Supplier";

    private static final String SELECT_BY_PHONE = SELECT_ALL_SUPPLIERS + " WHERE phone = ?";

    private PreparedStatement selectByPhone;
    
    public SupplierDB() throws DataAccessException {
        try {
        	selectByPhone = DBConnection.getInstance().getConnection().prepareStatement(SELECT_BY_PHONE);
		} catch (SQLException e) {
			throw new DataAccessException("Could not prepare statements for SupplierDB", e);
		}
    }
    	
	public Supplier findSupplierByPhone(String phone, boolean fullAssociation) throws DataAccessException	{
		try {
			selectByPhone.setString(1, phone);
			ResultSet rs = selectByPhone.executeQuery();
			return buildObject(rs, fullAssociation);
		} catch (SQLException e) {
			throw new DataAccessException("Could not find param or select invoice by No", e);
		}
	}

	private Supplier buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		Supplier s = null;
		try	{
			if (rs.next())	{
				s = new Supplier(
						rs.getString("phone"),
						rs.getString("name"),
						rs.getInt("addressId_FK"),
						rs.getString("email"),
						rs.getInt("cvr")
				);
				
				if (fullAssociation)	{
					//addresse metode
				}
			}
		} catch (SQLException e)	{
			throw new DataAccessException("Could not read result set for Supplier", e);
		}
		return s;
	}
	
}
