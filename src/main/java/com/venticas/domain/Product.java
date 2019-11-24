package com.venticas.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
	private int id;
	private String name;
	private String description;
	private float price;
	private int unitsOnStock;
	private List<ProductImage> imagenes;
	private ProductCategory category;
	private List<Family> families;
	
	public Product() {
		this.imagenes = new ArrayList<ProductImage>();
		this.category = new ProductCategory();
		this.families = new ArrayList<Family>();
	}


	public Product(int id, String name, String description, float price, int unitsOnStock){
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.unitsOnStock = unitsOnStock;
		this.imagenes = new ArrayList<ProductImage>();
		this.category = new ProductCategory();
		this.families = new ArrayList<Family>();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getUnitsOnStock() {
		return unitsOnStock;
	}
	public void setUnitsOnStock(int unitsOnStock) {
		this.unitsOnStock = unitsOnStock;
	}
	public List<ProductImage> getImagenes() {
		return imagenes;
	}
	public void setImagenes(List<ProductImage> imagenes) {
		this.imagenes = imagenes;
	}

	public List<Family> getFamilies() {
		return families;
	}

	public void setFamilies(List<Family> families) {
		this.families = families;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

}
