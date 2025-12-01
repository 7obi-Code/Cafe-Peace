package dao;

import interfaces.StockDBIF;
import modules.Stock;

import java.sql.*;
import java.time.LocalDateTime;

public class StockDB implements StockDBIF {

    private static final String SELECT_BY_PRODUCTID =
        "SELECT TOP 1 * FROM Stock WHERE product_FK = ? ORDER BY timestamp DESC";

    private static final String INSERT_STOCK =
        "INSERT INTO Stock (amount, product_FK, staff_FK) VALUES (?, ?, ?)";

    @Override
    public Stock findStockByProductId(int productId) throws SQLException, DataAccessException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_BY_PRODUCTID);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildStockFromResultSet(rs);
            } else {
                return null; // eller kast en custom exception
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void createStock(int productId, int amount, LocalDateTime timestamp)
            throws SQLException, DataAccessException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_STOCK);
            ps.setInt(1, amount);
            ps.setInt(2, productId);
            ps.setInt(3, 1001);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private Stock buildStockFromResultSet(ResultSet rs) throws SQLException {
        int stockId   = rs.getInt("stockId");
        int productId = rs.getInt("product_FK");
        int amount    = rs.getInt("amount");

        return new Stock(stockId, productId, amount);
    }
}
