package dao;

import interfaces.ProductDBIF;
import interfaces.StockDBIF;
import modules.Product;
import modules.Produce;
import modules.Beverages;
import modules.DryFoods;
import modules.Meat;
import modules.Stock;
import modules.Supplier;
import interfaces.SupplierDBIF;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

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

    @Override
    public Map<Product, Integer> updateStock(Map<Integer, Integer> addedQtyToProductId)
            throws SQLException, DataAccessException {

        Map<Product, Integer> result = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : addedQtyToProductId.entrySet()) {
            int productId     = entry.getKey();
            int depositAmount = entry.getValue();

            Stock currentStock = stockDB.findStockByProductId(productId);

            int currentAmount = (currentStock != null) ? currentStock.getAmount() : 0;
            int newAmount     = currentAmount + depositAmount;

            // Create new stock record
            stockDB.createStock(productId, newAmount, LocalDateTime.now());

            // Get strongly typed Product (subclass) — refactored to be abstract
            Product product = findProductById(productId, true);

            result.put(product, newAmount);
        }

        return result;
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

                if (fullAssociation && product != null) {
                    int productId = rs.getInt("productId");
                    Supplier supplier = supplierDB.findSupplierByPhone(rs.getString("supPhone_FK"), false);
                    product.setSupplier(supplier);
                    Stock stock = stockDB.findStockByProductId(productId);
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