package modules;

public class Beverages extends Product {
    private boolean hasSugar;
    private String size;
    
	public Beverages(int productId, String name, int minStock, int maxStock, String expiryDate, String unit,
			String prodType, boolean hasSugar, String size) {
		super(productId, name, minStock, maxStock, expiryDate, unit, prodType);
		this.hasSugar = hasSugar;
		this.size = size;
	}

	public boolean HasSugar() {
		return hasSugar;
	}

	public void setHasSugar(boolean hasSugar) {
		this.hasSugar = hasSugar;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
