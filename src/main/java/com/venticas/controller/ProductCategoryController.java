package com.venticas.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.venticas.business.ProductBusiness;
import com.venticas.business.ProductCategoryBusiness;
import com.venticas.business.TaxBusiness;
import com.venticas.domain.Product;
import com.venticas.domain.ProductCategory;
import com.venticas.domain.Tax;
import com.venticas.form.ProductCategoryForm;

import javax.validation.Valid;

@Controller
public class ProductCategoryController {

	@Autowired
	ProductCategoryBusiness productCategoryBusiness;
	@Autowired
	TaxBusiness taxBusiness;
	@Autowired
	ProductBusiness productBusiness;

	@RequestMapping(value = "/ProductCategory", method = RequestMethod.GET)
	public String productCategoryManager(Model model) {
		List<ProductCategory> productCategories = productCategoryBusiness.getAllCategories();
		model.addAttribute("productCategories", productCategories);
		return "productCategory/productCategoriesManager";
	}

	@RequestMapping(value = "/AddProductCategory", method = RequestMethod.GET)
	public String addProductCategory(Model model) {
		List<Product> products = new LinkedList<Product>();
		Product product = new Product();
		product.setId(1);
		product.setName("Telefono");
		products.add(product);

		product = new Product();
		product.setId(2);
		product.setName("Mircrohondas");
		products.add(product);

		ProductCategoryForm categoryForm = new ProductCategoryForm();
		model.addAttribute("categoryForm", categoryForm);
		model.addAttribute("products", products);
		List<Tax> taxes = taxBusiness.getAllTaxes();
		model.addAttribute("taxes", taxes);
		model.addAttribute("categories", productCategoryBusiness.getAllCategories());
		return "productCategory/addProductCategory";
	}

	@RequestMapping(value = "/AddProductCategory", method = RequestMethod.POST)
	public String addProductCategory(@Valid ProductCategoryForm productCategoryForm, BindingResult br, Model model) {
		//List<Integer> selectedProducts = productCategoryForm.getIdSelectedProducts();
		if(br.hasErrors()) {
			model.addAttribute("categoryForm", productCategoryForm);
			return "productCategory/addProductCategory";
		} else {
			Tax tax = taxBusiness.findTaxByID(productCategoryForm.getTax());
			ProductCategory category = new ProductCategory(productCategoryForm.getId(), productCategoryForm.getName(), tax);
			productCategoryBusiness.addCategory(category);
			return "redirect:/ProductCategory";
		}
	}

	@RequestMapping(value = "/UpdateProductCategory", method = RequestMethod.POST)
	public String updateProductCategory(Model model, @RequestParam("id") int id) {
		ProductCategory productCategory = productCategoryBusiness.findCategoryByID(id);
		List<Product> productsCategory = productBusiness.findByCategoryID(productCategory.getId());
		ProductCategoryForm productCategoryForm = new ProductCategoryForm(productCategory.getId(),productCategory.getName(), productCategory.getTax().getId());
		List<Tax> taxes = taxBusiness.getAllTaxes();
		model.addAttribute("taxes", taxes);
		model.addAttribute("productsCategory", productsCategory);
		model.addAttribute("productCategoryForm", productCategoryForm);
		return "ProductCategory/updateProductCategory";
	}

	@RequestMapping(value = "/DeleteProductCategory", method = RequestMethod.POST)
	public String deleteProductCategory(Model model, @RequestParam("id") int id) {
		ProductCategory productCategory = productCategoryBusiness.findCategoryByID(id);
		List<Product> productsCategory = productBusiness.findByCategoryID(productCategory.getId());
		ProductCategoryForm productCategoryForm = new ProductCategoryForm(productCategory.getId(),productCategory.getName(), productCategory.getTax().getId());
		List<Tax> taxes = taxBusiness.getAllTaxes();
		model.addAttribute("taxes", taxes);
		model.addAttribute("productsCategory", productsCategory);
		model.addAttribute("productCategoryForm", productCategoryForm);
		return "ProductCategory/deleteProductCategory";
	}

	@RequestMapping(value = "/UpdateProductCategoryConfirm", method = RequestMethod.POST)
	public String updateProductCategoryConfirm(Model model, ProductCategoryForm productCategoryForm) {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setId(productCategoryForm.getId());
		productCategory.setName(productCategoryForm.getName());
		productCategory.getTax().setId(productCategoryForm.getTax());
		productCategoryBusiness.updateCategory(productCategory);
		return "redirect:/ProductCategory";
	}

	@RequestMapping(value = "/DeleteProductCategoryConfirm", method = RequestMethod.POST)
	public String deleteProductCategoryConfirm(Model model, ProductCategoryForm productCategoryForm) {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setId(productCategoryForm.getId());
		productCategoryBusiness.deleteCategory(productCategory.getId());
		return "redirect:/ProductCategory";
	}

}
