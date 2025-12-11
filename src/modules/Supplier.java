package modules;

public class Supplier {
	private String phone;
	private String name;
	private int addressId;
	private String email;
	private int cvr;

	public Supplier(String phone, String name, int addressId, String email, int cvr) {
		super();
		this.phone = phone;
		this.name = name;
		this.addressId = addressId;
		this.email = email;
		this.cvr = cvr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCvr() {
		return cvr;
	}

	public void setCvr(int cvr) {
		this.cvr = cvr;
	}

}
