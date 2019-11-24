package com.venticas.data;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.venticas.domain.ProductImage;

@Repository
public class ProductImageData {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProductImage> getProductImages(int productID) {
        String sqlSelect = "SELECT ProductImageID, ProductID, FilePath" +
                " FROM ProductImage" +
                " WHERE ProductID = " + productID;
        return jdbcTemplate.query(sqlSelect, new ProductImageExtractor());
    }// fin getProductImages

    public void deleteImages(int productID) throws IOException {
        List<ProductImage> images = getProductImages(productID);
        for (ProductImage image: images) {
            Path path = FileSystems.getDefault().getPath(image.getFilePath());
            Files.delete(path);
        }
    }
} // fin ProductImageData


class ProductImageExtractor implements ResultSetExtractor<List<ProductImage>>
{
    @Override
    public List<ProductImage> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, ProductImage> map = new HashMap<>();
        ProductImage productImage = null;
        while (rs.next()) {
            Integer productImageID = rs.getInt("ProductImageID");
            productImage = map.get(productImageID);
            if(productImage == null) {
                productImage = new ProductImage();
                productImage.setId(productImageID);
                productImage.setFilePath(rs.getString("FilePath"));
                map.put(productImageID, productImage);
            }
        }
        return new ArrayList<>(map.values());
    }
}