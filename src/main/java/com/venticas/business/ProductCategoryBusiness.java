package com.venticas.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venticas.data.ProductCategoryData;
import com.venticas.domain.ProductCategory;

@Service
public class ProductCategoryBusiness {

	@Autowired
	ProductCategoryData productCategoryData;

	public List<ProductCategory> getAllCategories() {
		return productCategoryData.getCategories();
	}
	
	public void addCategory(ProductCategory category) {
		productCategoryData.addCategory(category);
	}

	public void updateCategory(ProductCategory category) {
		productCategoryData.updateCategory(category);
	}
	
	public void deleteCategory(int idCategory) {
		productCategoryData.deleteCategory(idCategory);
	}
	
	public ProductCategory findCategoryByName(String name) {
		return productCategoryData.findCategoryByName(name);
	}
	
	public ProductCategory findCategoryByID(int id) {
		return productCategoryData.findCategoryByID(id);
	}
}
