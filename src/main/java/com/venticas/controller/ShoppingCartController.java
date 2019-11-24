package com.venticas.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.venticas.business.CategoryBusiness;
import com.venticas.business.CustomerBusiness;
import com.venticas.business.FamilyBusiness;
import com.venticas.business.ProductBusiness;
import com.venticas.business.ShoppingCartBusiness;
import com.venticas.domain.Customer;
import com.venticas.domain.Item;
import com.venticas.domain.Product;
import com.venticas.domain.ShoppingCart;
import com.venticas.domain.User;
import com.venticas.form.ProductItemForm;

@Controller
public class ShoppingCartController {

	@Autowired
	ShoppingCartBusiness shoppingCartBusiness;
	@Autowired
	CustomerBusiness customerBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CategoryBusiness categoryBusiness;
	@Autowired
	FamilyBusiness familyBusiness;

	@RequestMapping(value = "/ViewShopping", method = RequestMethod.GET)
	public String viewShoppingCart(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		ShoppingCart cart = shoppingCartBusiness.getShopingCartByCustomerID(customer.getId());
		List<Item> items = shoppingCartBusiness.getItemsByShoppingCartID(cart.getId());
		List<Product> products = new LinkedList<Product>();
		for (Item item : items) {
			Product product = productBusiness.findProductById(item.getProduct().getId());
			product.setUnitsOnStock(item.getQuantity());
			products.add(product);
		}

		cart.setItems(items);
		session.setAttribute("cart", cart);
		model.addAttribute("products", products);
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
		return "shoppingCart/viewShoppingCart";
	}

	@RequestMapping(value = "/AddItemShoppingCart", method = RequestMethod.POST)
	public String viewShoppingCart(Model model, @RequestParam("id") int pid, @RequestParam("quantity") int quantity,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		ShoppingCart cart = shoppingCartBusiness.getShopingCartByCustomerID(customer.getId());
		Item item = new Item();
		item.setShoppingCart(cart);
		item.getProduct().setId(pid);
		item.setQuantity(quantity);
		shoppingCartBusiness.insertItem(item);
		return "redirect:/ViewShopping";
	}

	@RequestMapping(value = "/DeleteItem")
	public String DeleteItemCart(Model model, @RequestParam("id") int id, HttpSession session) {
		ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
		Item item = new Item();
		item.getProduct().setId(id);
		item.setShoppingCart(cart);
		shoppingCartBusiness.deleteItem(item);
		return "redirect:/ViewShopping";
	}

	@RequestMapping(value = "/UpdateItem", method = RequestMethod.POST)
	public String UpdateItemCart(Model model, @RequestParam("id") int id, HttpSession session) {
		ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
		Item item = shoppingCartBusiness.getItem(cart.getId(), id);
		ProductItemForm productItemForm = new ProductItemForm();
		Product product = productBusiness.findProductById(id);
		BeanUtils.copyProperties(product, productItemForm);
		productItemForm.setQuantity(item.getQuantity());
		model.addAttribute("product", product);
		model.addAttribute("productItemForm", productItemForm);
		return "product/productDetailModify";
	}

	@RequestMapping(value = "/UpdateItemShoppingCart", method = RequestMethod.POST)
	public String UpdateItemShoppingCart(Model model, ProductItemForm productItemForm, HttpSession session) {
		ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
		Item item = new Item();
		item.setShoppingCart(cart);
		item.getProduct().setId(productItemForm.getId());
		item.setQuantity(productItemForm.getQuantity());
		shoppingCartBusiness.updateItem(item);

		return "redirect:/ViewShopping";
	}

}
