package com.venticas.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.venticas.domain.ProductCategory;
import com.venticas.domain.ProductImage;

public class ProductItemForm {

	private int id;
	@NotNull
	@Size(min = 5, max = 50)
	private String name;
	@NotNull
	@Size(min = 10, max = 500)
	private String description;
	@NotNull
	@Min(1000)
	private float price;
	@Min(1)
	@Max(200)
	private int unitsOnStock;
	private ProductCategory category;
	private List<ProductImage> images;
	private List<Integer> families;
	@Min(1)
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProductItemForm() {
		images = new ArrayList<>();
		families = new ArrayList<>();
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

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public List<Integer> getFamilies() {
		return families;
	}

	public void setFamilies(List<Integer> families) {
		this.families = families;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
