package com.venticas.business;

import com.venticas.data.CategoryData;
import com.venticas.domain.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBusiness {

    @Autowired
    private CategoryData categoryData;

    public List<ProductCategory> findAll() {
        return categoryData.findAll();
    }

}
