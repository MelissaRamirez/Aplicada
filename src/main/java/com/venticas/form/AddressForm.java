package com.venticas.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddressForm {

	private int id;
	@NotNull
	@Size(min = 10, max = 50)
	private String addressLine;
	@NotNull
	@Size(min = 10, max = 50)
	private String city;
	@NotNull
	@Size(min = 10, max = 50)
	private String state;
	@NotNull
	@Size(min = 10, max = 20)
	private String postalCode;

	public AddressForm() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}


}
