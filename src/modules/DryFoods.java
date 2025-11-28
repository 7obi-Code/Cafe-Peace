package modules;

public class DryFoods extends Product {
	private String packageSize;
    private String dryFoodsType;
    
	public DryFoods(int productId, String name, int minStock, int maxStock, String expiryDate, String unit,
			String prodType, String packageSize, String dryFoodsType) {
		super(productId, name, minStock, maxStock, expiryDate, unit, prodType);
		this.packageSize = packageSize;
		this.dryFoodsType = dryFoodsType;
	}

	public String getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}

	public String getDryFoodsType() {
		return dryFoodsType;
	}

	public void setDryFoodsType(String dryFoodsType) {
		this.dryFoodsType = dryFoodsType;
	}

}
