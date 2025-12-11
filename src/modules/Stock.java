package modules;

import java.time.LocalDateTime;

public class Stock {
	private int stockId;
	private int productId;
	private int amount;
	private LocalDateTime timestamp;

	public Stock(int stockId, int productId, int amount, LocalDateTime timestamp) {
		this.stockId = stockId;
		this.productId = productId;
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public int getStockId() {
		return stockId;
	}

	public int getProductId() {
		return productId;
	}

	public int getAmount() {
		return amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
