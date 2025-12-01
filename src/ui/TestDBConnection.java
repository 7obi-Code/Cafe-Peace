package ui;
import java.util.*;
import modules.*;
import interfaces.*;
import dao.*;

public class TestDBConnection {
	private ProductDBIF productDB;
	
    public static void main(String[] args) throws DataAccessException {
    	productDB = new ProductDB();
        Product p = 


        // Print product & supplier details
        System.out.println("Product Details:");
        System.out.println("ID: " + p.getProductId());
        System.out.println("Name: " + p.getName());
        System.out.println("Supplier: " + (p.getSupplier() != null ? p.getSupplier().getName() : "None"));

        System.out.println("\nFull product object:");
        System.out.println(p);

        System.out.println("\nSupplier Details:");
        System.out.println("Name: " + s.getName());
        System.out.println("Phone: " + s.getPhone());
    }
}