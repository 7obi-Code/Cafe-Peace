package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import controllers.AlertCtrl;
import modules.Beverages;
import modules.Product;
import modules.Stock;

class TestCaseStockBoundary {

	@Test @DisplayName("TEST 1 - Max Boundary")
	void test1() throws Exception {
		// Arrange
		AlertCtrl alertCtrl = new AlertCtrl();
										//100 ER MAX STOCK PÅ PRODUKTET
		Product p = new Beverages(1000, "Banana", 10, 100, "101010", ".",
				"Beverage", false, "large");
					            //101 GÅR OVER MAKS STOCK
		Stock stock = new Stock(10, 1000, 101, LocalDateTime.now());
		p.setStock(stock);
		
		// Act
		boolean result = alertCtrl.checkMaxStock(p);
		
		// Assert 
		assertTrue(result);
	}

	@Test @DisplayName("TEST 2 - On Edge")
	void test2() throws Exception {
		// Arrange
		AlertCtrl alertCtrl = new AlertCtrl();
		Product p = new Beverages(1000, "Banana", 10, 100, "101010", ".",
				"Beverage", false, "large");
		                      //100 GÅR IKKE OVER MAKS STOCK
		Stock stock = new Stock(10, 1000, 100, LocalDateTime.now());
		p.setStock(stock);
		
		// Act
		boolean result = alertCtrl.checkMaxStock(p);
		
		// Assert 
		assertFalse(result);
	}
	
	@Test @DisplayName("TEST 3 - Min boundary")
	void test3() throws Exception {
		// Arrange
		AlertCtrl alertCtrl = new AlertCtrl();
		Product p = new Beverages(1000, "Banana", 10, 100, "101010", ".",
				"Beverage", false, "large");
		Stock stock = new Stock(10, 1000, 9, LocalDateTime.now());
		p.setStock(stock);
		
		// Act
		boolean result = alertCtrl.checkMinStock(p);
		
		// Assert 
		assertTrue(result);
	}
	
	@Test @DisplayName("TEST 4 - On Edge")
	void test4() throws Exception {
		// Arrange
		AlertCtrl alertCtrl = new AlertCtrl();
		Product p = new Beverages(1000, "Banana", 10, 100, "101010", ".",
				"Beverage", false, "large");
		Stock stock = new Stock(10, 1000, 10, LocalDateTime.now());
		p.setStock(stock);
		
		// Act
		boolean result = alertCtrl.checkMinStock(p);
		
		// Assert 
		assertFalse(result);
	}
	
}
