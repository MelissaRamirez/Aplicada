package com.venticas.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.venticas.business.CustomerBusiness;
import com.venticas.business.ShoppingCartBusiness;
import com.venticas.data.ProductData;
import com.venticas.domain.Address;
import com.venticas.domain.ContactInformation;
import com.venticas.domain.Customer;
import com.venticas.domain.ShoppingCart;
import com.venticas.domain.User;
import com.venticas.form.AddressForm;
import com.venticas.form.ContactInformationForm;

@Controller
public class CustomerController {

	@Autowired
	CustomerBusiness customerBusiness;
	@Autowired
	ProductData productData;
	@Autowired
	ShoppingCartBusiness shoppingCartBusiness;

	@RequestMapping(value = "/AddCustomerDetails", method = RequestMethod.GET)
	public String addCustomerDetails(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		AddressForm addressForm = new AddressForm();
		model.addAttribute("addressForm", addressForm);
		model.addAttribute("customer", customer);
		return "user/addCustomerDetailsAddress";
	}

	@RequestMapping(value = "/AddCustomerDetails", method = RequestMethod.POST)
	public String addCustomerDetails(Model model, HttpSession session, AddressForm addressForm) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		Address address = new Address();
		address.setAddressLine(addressForm.getAddressLine());
		address.setCity(addressForm.getCity());
		address.setState(addressForm.getState());
		address.setPostalCode(addressForm.getPostalCode());
		customer.getAddress().add(address);
		customerBusiness.addCustomerDetailsAddress(customer);
		return "redirect:/AddCustomerDetailsContactInfo";
	}

	@RequestMapping(value = "/AddCustomerDetailsContactInfo", method = RequestMethod.GET)
	public String addCustomerDetailsContinue(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		ContactInformationForm contactInformationForm = new ContactInformationForm();
		model.addAttribute("contactInformationForm", contactInformationForm);
		model.addAttribute("customer", user);
		return "user/addCustomerDetailsContactInfo";
	}

	@RequestMapping(value = "/AddCustomerDetailsContactInfo", method = RequestMethod.POST)
	public String addCustomerDetailsContinue(Model model, HttpSession session,
			ContactInformationForm contactInformationForm) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		ContactInformation contactInformation = new ContactInformation();
		contactInformation.setPhone(contactInformationForm.getPhone());
		contactInformation.setEmail(contactInformationForm.getEmail());
		customer.getContactInformations().add(contactInformation);
		customerBusiness.addCustomerDetailsContactInfo(customer);
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setDateCreated(new Date().toString());
		shoppingCart.setCustomer(customer);
		shoppingCartBusiness.insertShoppingCart(shoppingCart);
		return "redirect:/home";
	}

	@RequestMapping(value = "/SearchByProductName", method = RequestMethod.GET)
	public String SearchByProductName(Model model) {
		return "redirect:/findByName";
	}

	@RequestMapping(value = "/showAddress", method = RequestMethod.GET)
	public String showAddress(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<Address> listAddress = customerBusiness.getAllAddressByCustomerID(user.getId());
		model.addAttribute("addresses", listAddress);
		return "user/showAddress";
	}

	@RequestMapping(value = "/showContactInfo", method = RequestMethod.GET)
	public String showContactInfo(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<ContactInformation> listContact = customerBusiness.getAllContactInformationByCustomerID(user.getId());
		model.addAttribute("contactInfos", listContact);
		return "user/showContactInformation";
	}

	@RequestMapping(value = "/AddAddress", method = RequestMethod.GET)
	public String addAddress(Model model, HttpSession session) {
		AddressForm addressForm = new AddressForm();
		model.addAttribute("addressForm", addressForm);
		return "user/addAddress";
	}

	@RequestMapping(value = "/AddContactInfo", method = RequestMethod.GET)
	public String addContactInfo(Model model, HttpSession session) {
		ContactInformationForm contactInformationForm = new ContactInformationForm();
		model.addAttribute("contactInformationForm", contactInformationForm);
		return "user/addContactInfo";
	}

	@RequestMapping(value = "/AddAddress", method = RequestMethod.POST)
	public String addAddress(Model model, AddressForm addressForm, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		Address address = new Address();
		BeanUtils.copyProperties(addressForm, address);
		customer.getAddress().clear();
		customer.getAddress().add(address);
		customerBusiness.addCustomerDetailsAddress(customer);
		return "redirect:/home";
	}

	@RequestMapping(value = "/AddContactInfo", method = RequestMethod.POST)
	public String addContactInfoConfirm(Model model, ContactInformationForm contactInformationForm,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		ContactInformation contactInformation = new ContactInformation();
		BeanUtils.copyProperties(contactInformationForm, contactInformation);
		customer.getContactInformations().clear();
		customer.getContactInformations().add(contactInformation);
		customerBusiness.addCustomerDetailsContactInfo(customer);
		return "redirect:/home";
	}

	@RequestMapping(value = "/modifyAddress", method = RequestMethod.POST)
	public String updateAddress(Model model, @RequestParam("id") String id, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		Address address = customerBusiness.getAddress(customer.getId(), Integer.parseInt(id));
		AddressForm addressForm = new AddressForm();
		BeanUtils.copyProperties(address, addressForm);
		model.addAttribute("addressForm", addressForm);
		return "user/modifyAddress";
	}

	@RequestMapping(value = "/UpdateAddressConfirm", method = RequestMethod.POST)
	public String updateAddressConfirm(Model model, AddressForm addressForm, HttpSession session) {
		Address address = new Address();
		BeanUtils.copyProperties(addressForm, address);
		customerBusiness.updateCustomerAddress(address);
		return "redirect:/home";
	}

	@RequestMapping(value = "/modifyContactInfo", method = RequestMethod.POST)
	public String updateContactInfo(Model model, @RequestParam("id") int id, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Customer customer = customerBusiness.findCustomerByID(user);
		ContactInformation contactInformation = customerBusiness.getContactInfo(customer.getId(), id);
		ContactInformationForm contactInformationForm = new ContactInformationForm();
		BeanUtils.copyProperties(contactInformation, contactInformationForm);
		model.addAttribute("contactInformationForm", contactInformationForm);
		return "user/modifyContactInfo";
	}

	@RequestMapping(value = "/modifyContactInfoConfirm", method = RequestMethod.POST)
	public String updateContactInfoConfirm(Model model, ContactInformationForm contactInformationForm,
			HttpSession session) {
		ContactInformation contactInformation = new ContactInformation();
		BeanUtils.copyProperties(contactInformationForm, contactInformation);
		customerBusiness.updateCustomerContactInformation(contactInformation);
		return "redirect:/home";
	}
}
