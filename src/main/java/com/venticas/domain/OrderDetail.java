package com.venticas.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail {
	private int id;
	private int quantity;
	private float price;
	private List<Product> products;
	
	
	public OrderDetail() {
		this.products = new ArrayList<Product>();
	}
	public OrderDetail(int id, int quantity, float price) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.products = new ArrayList<Product>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	

}
