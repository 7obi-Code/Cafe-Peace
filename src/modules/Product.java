package modules;

public abstract class Product {
    private int productId;
    private String name;
    private int minStock;
    private int maxStock;
    private String expiryDate;
    private String unit;
    private String prodType;
    private Supplier supplier;
    private Stock stock;
    private int alertId;

    public Product(int productId, String name, int minStock, int maxStock, String expiryDate, String unit, String prodType) {
        this.productId = productId;
        this.name = name;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.expiryDate = expiryDate;
        this.unit = unit;
        this.prodType = prodType;
    }

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinStock() {
		return minStock;
	}

	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}

	public int getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(int maxStock) {
		this.maxStock = maxStock;
	}
	
	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public int getAlertId() {
		return alertId;
	}

	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}
    
}
