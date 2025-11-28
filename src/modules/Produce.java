package modules;

public class Produce extends Product {
    private String packageSize;
    private String produceType;
    
	public Produce(int productId, String name, int minStock, int maxStock, String expiryDate, String unit,
			String prodType, String packageSize, String produceType) {
		super(productId, name, minStock, maxStock, expiryDate, unit, prodType);
		this.packageSize = packageSize;
		this.produceType = produceType;
	}

	public String getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}

	public String getProduceType() {
		return produceType;
	}

	public void setProduceType(String produceType) {
		this.produceType = produceType;
	}
}
