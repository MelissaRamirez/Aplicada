package com.venticas.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.venticas.domain.ProductCategory;
import com.venticas.domain.Tax;

@Repository
public class CategoryData {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    //Se extraen todos las categorias de productos de la base de datos
    public List<ProductCategory> findAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sqlSelect =  "SELECT ProductCategoryID, ProductCategoryName, TaxID" +
                " FROM ProductCategory";
        return jdbcTemplate.query(sqlSelect, new CategogoriesExtractor());
    }
}

class CategogoriesExtractor implements ResultSetExtractor<List<ProductCategory>>
{

    @Override
    public List<ProductCategory> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, ProductCategory> map = new HashMap<>();
        ProductCategory category = null;
        while(rs.next()) {
            Integer idCategory = rs.getInt("ProductCategoryID");
            category = map.get(idCategory);
            if(category == null) {
                category = new ProductCategory(idCategory,
                        rs.getString("ProductCategoryName"),
                        new Tax());
                map.put(idCategory, category);
            }
        }
        return new ArrayList<ProductCategory>(map.values());
    }
}