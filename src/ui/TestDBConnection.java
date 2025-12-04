package ui;
import java.sql.SQLException;

import dao.DataAccessException;
import dao.ProductDB;
import interfaces.ProductDBIF;
import modules.Product;


public class TestDBConnection {
	
    @SuppressWarnings("unused")
	public static void main(String[] args) throws DataAccessException, SQLException {
    	ProductDBIF productDB = new ProductDB();
        int productId = 1;
    	Product p = productDB.findProductById(1, true);


        // Print product & supplier details
        System.out.println("Product Details:");
        System.out.println("ID: " + p.getProductId());
        System.out.println("Name: " + p.getName());
        System.out.println("Supplier: " + (p.getSupplier() != null ? p.getSupplier().getName() : "None"));

        System.out.println("\nFull product object:");
        System.out.println(p);

        System.out.println("\nSupplier Details:");
        System.out.println("Name: " + p.getSupplier().getName());
        System.out.println("Phone: " + p.getSupplier().getPhone());
        System.out.println("Email: " + p.getSupplier().getEmail());

        System.out.println("Amount: " + p.getStock().getAmount());
    }
}