package com.venticas.domain;

public class ProductCategory {
	private int id;
	private String name;
	private Tax tax;
	
	
	public ProductCategory() {
		this.tax = new Tax();
	}
	
	public ProductCategory(int id, String name, Tax tax) {
		super();
		this.id = id;
		this.name = name;
		this.tax = tax;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}
	
	

}
