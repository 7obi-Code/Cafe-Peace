package interfaces;

import java.sql.SQLException;
import java.util.Map;

import dao.DataAccessException;
import modules.Product;



public interface ProductDBIF {

	
	Product findProductById(int productId) throws SQLException, DataAccessException;

	
	
	//Opdater lageret udfra ændringer per produktId, et map med det nye samlet antal produkt returneres 
	Map<Product, Integer> updateStock(Map<Integer, Integer> qtyByProductId) throws SQLException, DataAccessException;

}
