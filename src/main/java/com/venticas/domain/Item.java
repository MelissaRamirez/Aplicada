package com.venticas.domain;

public class Item {

	Product product;
	ShoppingCart shoppingCart;
	int quantity;

	public Item() {
		this.product = new Product();
		this.shoppingCart = new ShoppingCart();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
