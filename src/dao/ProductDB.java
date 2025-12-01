package dao;

import interfaces.ProductDBIF;
import interfaces.StockDBIF;
import modules.Invoice;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class ProductDB implements ProductDBIF {
    private final StockDBIF stockDB;
    private final SupplierDBIF supplierDB;
    
    private static final String SELECT_ALL_PRODUCTS =
    		"SELECT productId, name, minStock, maxStock, expiryDate, unit, prodType, supPhone_FK, alert_FK FROM Product";

    	private static final String SELECT_BY_ID = SELECT_ALL_PRODUCTS + " WHERE productId = ?";

    	private static final String SELECT_PRODUCE = 
    		"SELECT packageSize, produceType FROM Produce WHERE productId_FK = ?";

    	private static final String SELECT_DRYFOODS =
    		"SELECT productId_FK, packageSize, dryFoodsType FROM DryFoods WHERE productId_FK = ?";

    	private static final String SELECT_BEVERAGE =
    		"SELECT productId_FK, hasSugar, size FROM Beverage WHERE productId_FK = ?";

    	private static final String SELECT_MEAT =
    		"SELECT productId_FK, animal, weight FROM Meat WHERE productId_FK = ?";
	
	private PreparedStatement selectById;
	private PreparedStatement selectProduce;
	private PreparedStatement selectDryFoods;
	private PreparedStatement selectBeverage;
	private PreparedStatement selectMeat;

    public ProductDB() throws DataAccessException {
        this.stockDB = new StockDB();
        this.supplierDB = new SupplierDB();
        try {
        	selectById = DBConnection.getInstance().getConnection().prepareStatement(SELECT_BY_ID);
        	selectProduce = DBConnection.getInstance().getConnection().prepareStatement(SELECT_PRODUCE);
			selectDryFoods = DBConnection.getInstance().getConnection().prepareStatement(SELECT_DRYFOODS);
			selectBeverage = DBConnection.getInstance().getConnection().prepareStatement(SELECT_BEVERAGE);
			selectMeat = DBConnection.getInstance().getConnection().prepareStatement(SELECT_MEAT);
		} catch (SQLException e) {
			throw new DataAccessException("Could not prepare statements for ProductDB", e);
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

            // Ny stock-record oprettes
            stockDB.createStock(productId, newAmount, LocalDateTime.now());

            // Hent produktet gennem Product
            Product product = findProductById(productId, true);

            result.put(product, newAmount);
        }

        return result;
    }
    
    public Product findProductById(int productId, boolean fullAssociation) throws DataAccessException {
		try {
			selectById.setInt(1, productId);
			ResultSet rs = selectById.executeQuery();
			return buildObject(rs, fullAssociation);
		} catch (SQLException e) {
			throw new DataAccessException("Could not find param or select invoice by No", e);
		}
    }

	private Product buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		Product p = null;
		try	{
			if (rs.next())	{
				p = new Product(
						rs.getInt("productId"),
						rs.getString("name"),
						rs.getInt("minStock"),
						rs.getInt("maxStock"),
						rs.getString("expiryDate"),
						rs.getString("unit"),
						rs.getString("prodType")
				);
				
				if (fullAssociation)	{
					p = buildType(p);
					Supplier supplier = supplierDB.findSupplierByPhone(rs.getString("supPhone_FK"), false);
					p.setSupplier(supplier);
					Stock stock = stockDB.findStockByProductId(rs.getInt("productId"));
					p.setStock(stock);
				}
			}
		} catch (SQLException e)	{
			throw new DataAccessException("Could not read result set for Product", e);
		}
		return p;
	}

	private Product buildType(Product base) throws DataAccessException {
		String type = base.getProdType();

		try {
			switch (type) {
				case "Produce":
					selectProduce.setInt(1, base.getProductId());
	                try (ResultSet rs = selectProduce.executeQuery()) {
	                    return loadProduce(rs, base);
	                }
				case "DryFoods":
					selectDryFoods.setInt(1, base.getProductId());
	                try (ResultSet rs = selectDryFoods.executeQuery()) {
	                    return loadDryFoods(rs, base);
	                }
				case "Beverage":
					selectBeverage.setInt(1, base.getProductId());
	                try (ResultSet rs = selectBeverage.executeQuery()) {
	                    return loadBeverage(rs, base);
	                }
				case "Meat":
					selectMeat.setInt(1, base.getProductId());
	                try (ResultSet rs = selectMeat.executeQuery()) {
	                    return loadMeat(rs, base);
	                }
				default:
					return base;
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not load type for products", e);
		}
	}
	
    private Produce loadProduce(ResultSet rs, Product base) throws DataAccessException {
        Produce produce = null;
    	try {
            if (rs.next()) {
            	produce = new Produce(
                    base.getProductId(),
                    base.getName(),
                    base.getMinStock(),
                    base.getMaxStock(),
                    base.getExpiryDate(),
                    base.getUnit(),
                    base.getProdType(),
                    rs.getString("packageSize"),
                    rs.getString("produceType")
                );
            }
        } catch (SQLException e) {
			throw new DataAccessException("Could not load type for produce", e);
		}
        return produce;
    }
    
    private DryFoods loadDryFoods(ResultSet rs, Product base) throws DataAccessException {
        DryFoods dryFoods = null;
        try {
            if (rs.next()) {
                dryFoods = new DryFoods(
                    base.getProductId(),
                    base.getName(),
                    base.getMinStock(),
                    base.getMaxStock(),
                    base.getExpiryDate(),
                    base.getUnit(),
                    base.getProdType(),
                    rs.getString("packageSize"),
                    rs.getString("dryFoodsType")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not load type for product dryfoods", e);
        }
        return dryFoods;
    }

    private Beverages loadBeverage(ResultSet rs, Product base) throws DataAccessException {
        Beverages beverage = null;
        try {
            if (rs.next()) {
                beverage = new Beverages(
                    base.getProductId(),
                    base.getName(),
                    base.getMinStock(),
                    base.getMaxStock(),
                    base.getExpiryDate(),
                    base.getUnit(),
                    base.getProdType(),
                    rs.getBoolean("hasSugar"),
                    rs.getString("size")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not load type for product beverage", e);
        }
        return beverage;
    }

    private Meat loadMeat(ResultSet rs, Product base) throws DataAccessException {
        Meat meat = null;
        try {
            if (rs.next()) {
                meat = new Meat(
                    base.getProductId(),
                    base.getName(),
                    base.getMinStock(),
                    base.getMaxStock(),
                    base.getExpiryDate(),
                    base.getUnit(),
                    base.getProdType(),
                    rs.getDouble("weight"),
                    rs.getString("animal")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not load type for product meat", e);
        }
        return meat;
    }
}
