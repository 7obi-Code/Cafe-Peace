package modules;

import java.time.LocalDateTime;


public class Stock {
	private int stockId;
	private Product product;
	private int amount;
	private LocalDateTime timestamp;
	
		
		
		
		
	// Bruges når vi læser fra databasen
    public Stock(int stockId, Product product, int amount, LocalDateTime timestamp) {
        this.stockId = stockId;
        this.product = product;
        this.amount = amount;
        this.timestamp = timestamp;
    }
    
 // Getters
    public int getStockId() {
        return stockId;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
}
    