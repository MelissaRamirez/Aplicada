package com.venticas.business;

import com.venticas.data.ProductData;
import com.venticas.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductBusiness {

    @Autowired
    private ProductData productData;

    public List<Product> findByFamilyID(int familyID) {
        return  productData.findProductByFamily(familyID);
    }

   /* public boolean updateProduct(Product product) {
        return productData.updateProduct(product);
    }*/

    public void insertProduct(Product product) {
        productData.insertProduct(product);
    }

    public List<Product> findByName(String name) {
        return productData.findByName(name);
    }
    
    public List<Product> findByCategoryID(int idCategory) {
		return productData.findByCategoryID(idCategory);
	}

    public boolean deleteProduct(int productID) {
        return productData.deleteProduct(productID);
    }

    public Product findProductById(int productID) {
        return productData.findProductById(productID);
    }
}
