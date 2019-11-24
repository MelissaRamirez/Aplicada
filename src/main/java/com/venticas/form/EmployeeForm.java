package com.venticas.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmployeeForm {

	private int id;
	@NotNull
	@Size(min = 10, max = 500)
	private String email;
	@NotNull
	@Size(min = 10, max = 500)
	private String phone;
	private boolean active;

	public EmployeeForm() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
