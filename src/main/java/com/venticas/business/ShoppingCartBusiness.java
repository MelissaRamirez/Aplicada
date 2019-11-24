package com.venticas.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venticas.data.ShoppingCartData;
import com.venticas.domain.Item;
import com.venticas.domain.ShoppingCart;

@Service
public class ShoppingCartBusiness {

	@Autowired
	ShoppingCartData shoppingCartData;
	
	public void insertShoppingCart(ShoppingCart shoppingCart) {
		shoppingCartData.insertShoppingCart(shoppingCart);
	}
	
	public void insertItem(Item item) {
		shoppingCartData.insertItem(item);
	}
	
	public void updateItem(Item item) {
		shoppingCartData.updateItem(item);
	}
	
	public void deleteItem(Item item) {
		shoppingCartData.deleteItem(item);
	}
	
	public List<Item> getAllItem(ShoppingCart shoppingCart){
		return shoppingCartData.selectAllItems(shoppingCart);
	}
	
	public ShoppingCart getShopingCartByCustomerID(int id) {
		return shoppingCartData.getShopingCartByCustomerID(id);
	}
	
	public List<Item> getItemsByShoppingCartID(int id) {
		return shoppingCartData.getItemsByShoppingCartID(id);
	}
	
	public Item getItem(int cartId, int productId) {
		return shoppingCartData.getItem(cartId, productId);
	}
	
}
