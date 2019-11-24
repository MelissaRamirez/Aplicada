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

import com.venticas.domain.Family;

@Repository
public class FamilyData {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;


    public List<Family> findAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sqlSelect =  "SELECT FamilyID, FamilyName" +
                " FROM Family";
        return jdbcTemplate.query(sqlSelect, new ProductFamiliesExtractor());
    }

    public List<Family> findFamilyByProductId(int productID) {
        String sqlSelect = "SELECT f.FamilyID, FamilyName" +
                " FROM Family f JOIN Family_Product fp ON f.FamilyID = fp.FamilyID" +
                " WHERE ProductID = " + productID;
        return jdbcTemplate.query(sqlSelect, new ProductFamiliesExtractor());
    }
}

class ProductFamiliesExtractor implements ResultSetExtractor<List<Family>>
{
    @Override
    public List<Family> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Family> map = new HashMap<>();
        Family family = null;
        while (rs.next()) {
            Integer familyID = rs.getInt("FamilyID");
            family = map.get(familyID);
            if(family == null) {
                family = new Family();
                family.setId(familyID);
                family.setName(rs.getString("FamilyName"));
                map.put(familyID, family);
            }
        }
        return new ArrayList<Family>(map.values());
    }
}
