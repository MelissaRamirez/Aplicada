package com.venticas.business;

import com.venticas.data.FamilyData;
import com.venticas.domain.Family;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyBusiness {

    @Autowired
    private FamilyData familyData;

    public List<Family> findAll() {
        return familyData.findAll();
    }

}
