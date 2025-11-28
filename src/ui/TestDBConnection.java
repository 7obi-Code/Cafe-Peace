package ui;

import dao.DataAccessException;
import dao.ProductDB;
import modules.Product;
import modules.Produce;
import modules.DryFoods;
import modules.Beverages;
import modules.Meat;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            ProductDB productDB = new ProductDB();

            // Try loading different product IDs (replace with IDs that exist in your DB)
            Product p1 = productDB.findProductById(1, true); // assume ID 1 is Produce
            printProduct(p1);

            Product p2 = productDB.findProductById(2, true); // assume ID 2 is DryFoods
            printProduct(p2);

            Product p3 = productDB.findProductById(3, true); // assume ID 3 is Beverage
            printProduct(p3);

            Product p4 = productDB.findProductById(4, true); // assume ID 4 is Meat
            printProduct(p4);

            Product p5 = productDB.findProductById(5, false); // base Product only
            printProduct(p5);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private static void printProduct(Product p) {
        if (p == null) {
            System.out.println("No product found.");
            return;
        }

        System.out.println("Loaded product: " + p.getName() + " (type=" + p.getProdType() + ")");

        if (p instanceof Produce) {
            Produce pr = (Produce) p;
            System.out.println("  ProduceType: " + pr.getProduceType() + ", PackageSize: " + pr.getPackageSize());
        } else if (p instanceof DryFoods) {
            DryFoods df = (DryFoods) p;
            System.out.println("  DryFoodsType: " + df.getDryFoodsType() + ", PackageSize: " + df.getPackageSize());
        } else if (p instanceof Beverages) {
            Beverages b = (Beverages) p;
            System.out.println("  Beverage Size: " + b.getSize() + ", HasSugar: " + b.HasSugar());
        } else if (p instanceof Meat) {
            Meat m = (Meat) p;
            System.out.println("  Animal: " + ", Weight: ");
        }
    }
}
