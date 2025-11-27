package modules;

public class InvoiceLine {
	private int quantity;
	private Product product;
	
	public InvoiceLine(int quantity, Product product)	{
		this.quantity = quantity;
		this.product = product;
	}
}
