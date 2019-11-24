package com.venticas.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.venticas.business.TaxBusiness;
import com.venticas.domain.Tax;
import com.venticas.form.TaxForm;

import javax.validation.Valid;

@Controller
public class TaxController {

	@Autowired
	TaxBusiness taxBusiness;

	@RequestMapping(value = "/Tax", method = RequestMethod.GET)
	public String viewTaxes(Model model) {
		List<Tax> taxes = taxBusiness.getAllTaxes();
		model.addAttribute("taxes", taxes);
		return "tax/Tax";
	}

	@RequestMapping(value = "/AddTax", method = RequestMethod.GET)
	public String addTax(Model model) {
		TaxForm taxForm = new TaxForm();
		model.addAttribute("taxForm", taxForm);
		return "tax/addTax";
	}

	@RequestMapping(value = "/AddTax", method = RequestMethod.POST)
	public String addTax(@Valid TaxForm taxForm, BindingResult br, Model model) {
		if(br.hasErrors()) {
			return "tax/addTax";
		} else {
			Tax tax = new Tax();
			BeanUtils.copyProperties(taxForm, tax);
			taxBusiness.addTax(tax);
			return "redirect:/Tax";
		}
	}

	@RequestMapping(value = "/UpdateTax", method = RequestMethod.POST)
	public String updateTax(Model model, @RequestParam("id") int idTax) {
		Tax tax = taxBusiness.findTaxByID(idTax);
		TaxForm taxForm = new TaxForm();
		taxForm.setId(tax.getId());
		taxForm.setType(tax.getType());
		taxForm.setPercentage(tax.getPercentage());
		model.addAttribute("taxForm", taxForm);
		return "tax/updateTax";
	}
	
	@RequestMapping(value = "/DeleteTax", method = RequestMethod.POST)
	public String deleteTax(Model model, @RequestParam("id") int idTax) {
		Tax tax = taxBusiness.findTaxByID(idTax);
		TaxForm taxForm = new TaxForm();
		taxForm.setId(tax.getId());
		taxForm.setType(tax.getType());
		taxForm.setPercentage(tax.getPercentage());
		model.addAttribute("taxForm", taxForm);
		return "tax/deleteTax";
	}
	
	@RequestMapping(value = "/UpdateTaxConfirm", method = RequestMethod.POST)
	public String updateTaxConfirm(@Valid TaxForm taxForm, BindingResult br, Model model) {
		if(br.hasErrors()) {
			return "tax/updateTax";
		} else {
			Tax tax = taxBusiness.findTaxByID(taxForm.getId());
			tax.setType(taxForm.getType());
			tax.setPercentage(taxForm.getPercentage());
			taxBusiness.updateTax(tax);
			return "redirect:/Tax";
		}
	}
	
	@RequestMapping(value = "/DeleteTaxConfirm", method = RequestMethod.POST)
	public String deleteTaxConfirm(Model model, TaxForm taxForm) {
		Tax tax = taxBusiness.findTaxByID(taxForm.getId());
		taxBusiness.deleteTax(tax.getId());
		return "redirect:/Tax";
	}
}
