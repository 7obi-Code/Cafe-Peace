package modules;

public class Product {

    private int productId;
    private String name;
    private int minStock;
    private int maxStock;

    public Product(int productId, String name, int minStock, int maxStock) {
        this.productId = productId;
        this.name = name;
        this.minStock = minStock;
        this.maxStock = maxStock;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getMinStock() {
        return minStock;
    }

    public int getMaxStock() {
        return maxStock;
    }
}
