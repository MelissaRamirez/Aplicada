package com.venticas.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.venticas.data.CustomerData;
import com.venticas.domain.Address;
import com.venticas.domain.ContactInformation;
import com.venticas.domain.Customer;
import com.venticas.domain.User;

@Repository
public class CustomerBusiness {

	@Autowired
	CustomerData customerData;

	public Customer findCustomerByID(User user) {
		return customerData.findCustomerByID(user);
	}

	public void addCustomerDetailsAddress(Customer customer) {
		customerData.addCustomerDetailsAddress(customer);
	}
	
	public void addCustomerDetailsContactInfo(Customer customer) {
		customerData.addCustomerDetailsContactInfo(customer);
	}
	
	public boolean firstLoginCustomer(User user) {
		return customerData.firstLoginCustomer(user);
	}
	
	public void updateCustomerAddress(Address address){
		customerData.updateCustomerAddress(address);
	}
	
	public void deleteCustomerByID(int id) {
		customerData.deleteCustomerByID(id);
	}
	
	public void updateCustomerContactInformation(ContactInformation contacti){
		customerData.updateCustomerContactInformation(contacti);
	}
	
	public List<ContactInformation> getAllContactInformationByCustomerID(int idCustomer) {
		return customerData.getAllContactInformationByCustomerID(idCustomer);
	}
	
	public List<Address> getAllAddressByCustomerID(int idCustomer) {
		return customerData.getAllAddressByCustomerID(idCustomer);
	}
	
	public Address getAddress(int idCustomer, int idAddress) {
		return customerData.getAddress(idCustomer, idAddress);
	}
	
	public ContactInformation getContactInfo(int idCustomer, int contactID) {
		return customerData.getAllContactInformation(idCustomer, contactID);
	}
	
}
