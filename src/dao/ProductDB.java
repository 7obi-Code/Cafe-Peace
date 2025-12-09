package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import interfaces.ProductDBIF;
import interfaces.StockDBIF;
import interfaces.SupplierDBIF;
import modules.Beverages;
import modules.DryFoods;
import modules.Meat;
import modules.Produce;
import modules.Product;
import modules.Stock;
import modules.Supplier;

public class ProductDB implements ProductDBIF {
    private final StockDBIF stockDB;
    private final SupplierDBIF supplierDB;

    // SELECT * with table aliases
    private static final String SELECT_ALL_PRODUCTS_WITH_TYPE_INFO =
        "SELECT * FROM Product p " +
        "LEFT JOIN Produce pr ON p.productId = pr.productId_FK " +
        "LEFT JOIN DryFoods df ON p.productId = df.productId_FK " +
        "LEFT JOIN Beverage b ON p.productId = b.productId_FK " +
        "LEFT JOIN Meat m ON p.productId = m.productId_FK ";

    private static final String SELECT_BY_ID =
        SELECT_ALL_PRODUCTS_WITH_TYPE_INFO + " WHERE p.productId = ?";

    private PreparedStatement selectById;

    public ProductDB() throws DataAccessException {
        this.stockDB = new StockDB();
        this.supplierDB = new SupplierDB();
        try {
            selectById = DBConnection.getInstance().getConnection().prepareStatement(SELECT_BY_ID);
        } catch (SQLException e) {
            throw new DataAccessException("Could not prepare statement for ProductDB", e);
        }
    }
    
    //updateStock der laver en ny Stock til de produkter der er blevet opdateret ud fra hvad brugeren skrev i UI
    //Dette er lavet som transaction, da det hele gerne skal ske i et hug så noget ikke bliver opdateret uden andet ikke gør.
    @Override
    public void updateStockDeposit(HashMap<Integer, Integer> addedQtyToProductId) throws DataAccessException {
    	
        Connection connection = DBConnection.getInstance().getConnection();
        
        try	{
        connection.setAutoCommit(false);
        
        for (HashMap.Entry<Integer, Integer> entry : addedQtyToProductId.entrySet()) {
            int productId     = entry.getKey();
            int depositAmount = entry.getValue();

            Stock currentStock = stockDB.findStockByProductId(productId);

            int currentAmount = (currentStock != null) ? currentStock.getAmount() : 0;
            int newAmount     = currentAmount + depositAmount;

            // Create new stock for a product
            stockDB.createStock(productId, newAmount, LocalDateTime.now());

        }
        
        connection.commit();
        connection.setAutoCommit(true);
        
        } catch (SQLException e)	{
        	if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                    rollbackEx.printStackTrace();
                }
        	}
        }
    }
    
    public void updateStockWithdraw(Product product, int withdrawQty) throws DataAccessException	{
    	try {
    		int productId = product.getProductId();
    		int newAmount = product.getStock().getAmount() + withdrawQty;
    		
    		stockDB.createStock(productId, newAmount, LocalDateTime.now());
    		
    	} catch (SQLException e)	{
    		throw new DataAccessException("Kunne ikke opdatere produktets stock ved withdraw", e);
    	}
    }
    
    @Override
    public Product findProductById(int productId, boolean fullAssociation) throws DataAccessException {
        try {
            selectById.setInt(1, productId);
            ResultSet rs = selectById.executeQuery();
            return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find parameter or select product by ID", e);
        }
    }

    private Product buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
        try {
            if (rs.next()) {
                String prodType = rs.getString("prodType");
                Product product = null;
                switch (prodType) {
                    case "Produce":
                        product = buildProduce(rs);
                        break;
                    case "DryFoods":
                        product = buildDryFoods(rs);
                        break;
                    case "Beverage":
                        product = buildBeverage(rs);
                        break;
                    case "Meat":
                        product = buildMeat(rs);
                        break;
                }

                if (fullAssociation) {
                    Supplier supplier = supplierDB.findSupplierByPhone(rs.getString("supPhone_FK"), false);
                    product.setSupplier(supplier);
                    Stock stock = stockDB.findStockByProductId(rs.getInt("productId"));
                    product.setStock(stock);
                }

                return product;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not read result set for Product", e);
        }
        return null;
    }

    private Produce buildProduce(ResultSet rs) throws DataAccessException {
    	try {
    		return new Produce(
    				rs.getInt("productId"),
    				rs.getString("name"),
    				rs.getInt("minStock"),
    				rs.getInt("maxStock"),
    				rs.getString("expiryDate"),
    				rs.getString("unit"),
    				rs.getString("prodType"),
    				rs.getString("packageSize"),
    				rs.getString("produceType")
    				);
    	} catch (SQLException e) {
    		throw new DataAccessException("Could not read result set for Beverage", e);
    	}
    }
    
    private DryFoods buildDryFoods(ResultSet rs) throws DataAccessException {
    	try {
    		return new DryFoods(
    				rs.getInt("productId"),
    				rs.getString("name"),
    				rs.getInt("minStock"),
    				rs.getInt("maxStock"),
    				rs.getString("expiryDate"),
    				rs.getString("unit"),
    				rs.getString("prodType"),
    				rs.getString("packageSize"),
    				rs.getString("dryFoodsType")
    				);
    	} catch (SQLException e) {
    		throw new DataAccessException("Could not read result set for Beverage", e);
    	}
    }
    
    private Beverages buildBeverage(ResultSet rs) throws DataAccessException {
    	try {
    		return new Beverages(
    				rs.getInt("productId"),
    				rs.getString("name"),
    				rs.getInt("minStock"),
    				rs.getInt("maxStock"),
    				rs.getString("expiryDate"),
    				rs.getString("unit"),
    				rs.getString("prodType"),
    				rs.getBoolean("hasSugar"),
    				rs.getString("size")
    				);
    	} catch (SQLException e) {
    		throw new DataAccessException("Could not read result set for Beverage", e);
    	}
    }
    
    private Meat buildMeat(ResultSet rs) throws DataAccessException {
    	try {
    		return new Meat(
    				rs.getInt("productId"),
    				rs.getString("name"),
    				rs.getInt("minStock"),
    				rs.getInt("maxStock"),
    				rs.getString("expiryDate"),
    				rs.getString("unit"),
    				rs.getString("prodType"),
    				rs.getDouble("weight"),
    				rs.getString("animal")
    				);
    	} catch (SQLException e) {
    		throw new DataAccessException("Could not read result set for Product", e);
    	}
    }
}