package com.venticas.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

public class ProductCategoryForm {

	private int id;
	@NotNull
	@Size(min = 5, max = 40)
	private String name;
	private int tax;
	private List<Integer> idSelectedProducts;

	public ProductCategoryForm() {
		this.idSelectedProducts = new LinkedList<Integer>();
	}

	public ProductCategoryForm(int id, String name, int tax) {
		super();
		this.id = id;
		this.name = name;
		this.tax = tax;
		this.idSelectedProducts = new LinkedList<Integer>();
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

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public List<Integer> getIdSelectedProducts() {
		return idSelectedProducts;
	}

	public void setIdSelectedProducts(List<Integer> idSelectedProducts) {
		this.idSelectedProducts = idSelectedProducts;
	}

}
