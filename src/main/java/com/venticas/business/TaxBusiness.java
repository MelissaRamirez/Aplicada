/*
 * Trabajada por Luis
 */
package com.venticas.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venticas.data.TaxData;
import com.venticas.domain.Tax;

/**
 * The Class TaxBusiness.
 * <p>
 * Esta clase se encarga de aplicar las reglas de negocios y llamar a la capa de
 * datos
 * </p>
 */
@Service
public class TaxBusiness {

	@Autowired
	TaxData taxData;

	public List<Tax> getAllTaxes() {
		return taxData.getAllTaxes();
	}

	public void addTax(Tax tax) {
		taxData.addTax(tax);
	}

	public void updateTax(Tax tax) {
		taxData.updateTax(tax);
	}
	
	public void deleteTax(int idTax) {
		taxData.deleteTax(idTax);
	}

	public Tax findTaxByType(String type) {
		return taxData.findTaxByType(type);
	}
	
	public Tax findTaxByID(int id) {
		return taxData.findTaxByID(id);
	}
}
