package modules;

public class Meat extends Product {
	private double weight;
	private String animal;
	
	public Meat(int productId, String name, int minStock, int maxStock, String expiryDate, String unit,
			String prodType, double weight, String animal) {
		super(productId, name, minStock, maxStock, expiryDate, unit, prodType);
		this.weight = weight;
		this.animal = animal;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

}
