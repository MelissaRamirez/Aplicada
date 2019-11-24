package com.venticas.business;

import com.venticas.data.ProductImageData;
import com.venticas.domain.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageBusiness {

    @Autowired
    private ProductImageData productImageData;

    public List<ProductImage> getProductImages(int productID) {
        return productImageData.getProductImages(productID);
    }

}
