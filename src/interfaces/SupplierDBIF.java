package interfaces;
import dao.DataAccessException;
import modules.Supplier;

public interface SupplierDBIF {
	Supplier findSupplierByPhone(String phone, boolean fullAssociation) throws DataAccessException;
}
