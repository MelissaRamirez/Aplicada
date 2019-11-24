package com.venticas.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {

	private int id;
	@NotNull
	@Size(min = 8, max = 50)
	private String identification;
	@NotNull
	@Size(min = 2, max = 50)
	private String firstName;
	@NotNull
	@Size(min = 2, max = 50)
	private String lastName;
	@NotNull
	@Size(min = 4, max = 70)
	private String userName;
	@NotNull
	@Size(min = 6, max = 50)
	private String password;
	private boolean active;

	public UserForm() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
