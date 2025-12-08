package interfaces;

import java.sql.SQLException;
import java.util.HashMap;

import dao.DataAccessException;
import modules.Product;



public interface ProductDBIF {

	Product findProductById(int productId, boolean fullAssociation) throws SQLException, DataAccessException;

	//Opdater lageret udfra ændringer per produktId, et map med det nye samlet antal produkt returneres 
	void updateStockDeposit(HashMap<Integer, Integer> qtyByProductId) throws DataAccessException;

}
