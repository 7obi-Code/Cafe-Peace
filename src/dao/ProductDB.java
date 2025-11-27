package dao;

import interfaces.ProductDBIF;
import interfaces.StockDBIF;
import modules.Product;
import modules.Stock;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProductDB implements ProductDBIF {
    private final StockDBIF stockDB;

    public ProductDB() {
        this.stockDB = new StockDB();
    }

    @Override
    public Map<Product, Integer> updateStock(Map<Integer, Integer> qtyByProductId)
            throws SQLException, DataAccessException {

        Map<Product, Integer> result = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : qtyByProductId.entrySet()) {
            int productId = entry.getKey();
            int depositAmount = entry.getValue();

            // Nuværende Stock + produkt
            Stock currentStock = stockDB.findStockByProductId(productId);
            Product product    = currentStock.getProduct();
            int newAmount      = currentStock.getAmount() + depositAmount;

            // Ny stock-record oprettes
            stockDB.createStock(productId, newAmount, LocalDateTime.now().toString());

            result.put(product, newAmount);
        }

        return result;
    }
}
