package Utilities;

import controllers.ProductCtrl;

public class UserThread extends Thread {
	private final ProductCtrl ProductCtrl;
	private String Username;
	
	
	public UserThread(ProductCtrl ctrl, String username) {
		this.ProductCtrl = ctrl;
		this.Username = username;
	}
	
	public UserThread(ProductCtrl ctrl) {
		this.ProductCtrl = ctrl;
	}
	
	@Override
	public void run() {
		System.out.println("Thread startet for: " + this.Username);
		try {
			ProductCtrl.confirmWithdraw(1002, 20, this.Username);
		} catch (Exception e) {
			System.out.println("Product could not be withdrawed for " + this.Username);
			e.printStackTrace();
		}
	}
}
 