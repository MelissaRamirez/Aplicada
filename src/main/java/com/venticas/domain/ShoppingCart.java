package com.venticas.domain;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
	private int id;
	private String dateCreated;
	private List<Item> items;
	private Customer customer;

	public ShoppingCart() {
		this.items = new ArrayList<Item>();
		this.customer = new Customer();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
