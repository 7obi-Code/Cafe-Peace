package modules;

import java.time.LocalDateTime;


public class Stock {
    private int stockId;
    private int productId;
    private int amount;

    public Stock(int stockId, int productId, int amount) {
        this.stockId = stockId;
        this.productId = productId;
        this.amount = amount;
    }

    public int getStockId()      { return stockId; }
    public int getProductId()    { return productId; }
    public int getAmount()       { return amount; }


    public void setStockId(int stockId)           { this.stockId = stockId; }
    public void setProductId(int productId)       { this.productId = productId; }
    public void setAmount(int amount)             { this.amount = amount; }
}
