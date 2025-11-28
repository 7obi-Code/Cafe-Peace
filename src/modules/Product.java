package modules;

public class Product {
    private int productId;
    private String name;
    private int minStock;
    private int maxStock;
    private String expiryDate;
    private String unit;
    private String prodType;
    private String supPhone;
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

	public String getSupPhone() {
		return supPhone;
	}

	public void setSupPhone(String supPhone) {
		this.supPhone = supPhone;
	}

	public int getAlertId() {
		return alertId;
	}

	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}
    
}
