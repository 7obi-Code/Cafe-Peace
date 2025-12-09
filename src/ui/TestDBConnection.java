package ui;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.ProductCtrl;
import dao.DataAccessException;
import dao.ProductDB;
import interfaces.ProductDBIF;
import modules.Product;


public class TestDBConnection {
	
    @SuppressWarnings("unused")
	public static void main(String[] args) throws DataAccessException, SQLException {
    	ProductCtrl productCtrl = new ProductCtrl();

        System.out.println("Loading all products...\n");

        ArrayList<Product> products = productCtrl.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found in database.");
            return;
        }

        for (Product p : products) {
            System.out.println("-------------------------------------------------");
            System.out.println("Product ID: " + p.getProductId());
            System.out.println("Name: " + p.getName());
            System.out.println("Type: " + p.getProdType());

            if (p.getSupplier() != null) {
                System.out.println("Supplier: " + p.getSupplier().getName() +
                                   " (" + p.getSupplier().getPhone() + ")");
            } else {
                System.out.println("Supplier: None");
            }

            if (p.getStock() != null) {
                System.out.println("Stock Amount: " + p.getStock().getAmount());
            } else {
                System.out.println("Stock: None");
            }

            System.out.println("Full object:");
            System.out.println(p);
        }

        System.out.println("\nDone.");
    }
}