package dao;

import interfaces.StockDBIF;
import modules.Stock;

import java.sql.*;
import java.time.LocalDateTime;

public class StockDB implements StockDBIF {

    private static final String SELECT_BY_PRODUCTID =
        "SELECT TOP 1 * FROM Stock WHERE productId = ? ORDER BY timestamp DESC";

    private static final String INSERT_STOCK =
        "INSERT INTO Stock (productId, amount, timestamp) VALUES (?, ?, ?)";

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
                // Hvis der ikke findes nogen record endnu, kan du selv vælge:
                // 1) returnere null, eller
                // 2) kaste en exception
                return null;
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void createStock(int productId, int amount, String timestamp)
            throws SQLException, DataAccessException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_STOCK);
            ps.setInt(1, productId);
            ps.setInt(2, amount);
            ps.setString(3, timestamp);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private Stock buildStockFromResultSet(ResultSet rs) throws SQLException {
        int id        = rs.getInt("id");         // hvis du har en PK
        int productId = rs.getInt("productId");
        int amount    = rs.getInt("amount");
        Timestamp ts  = rs.getTimestamp("timestamp");

        // Tilpas
        return new Stock(id, productId, amount, ts.toLocalDateTime());
    }
}
