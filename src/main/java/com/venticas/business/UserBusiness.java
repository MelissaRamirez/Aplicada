package com.venticas.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.venticas.data.UserData;
import com.venticas.domain.User;

@Service
public class UserBusiness {

	@Autowired
	UserData userData;

	public void addUserCustomer(User user) {
		encrypt(user);
		userData.addUserCustomer(user);
	}

	public User findUserByUserName(String userName) {
		return userData.findUserByUserName(userName);
	}

	public List<User> getUserManager() {
		return userData.getUserManager();
	}

	public List<User> getUserEmployees() {
		return userData.getUserEmployee();
	}

	public List<User> getOrganization() {
		return userData.getOrganization();
	}

	public List<User> getUserCustomer() {
		return userData.getCustomer();
	}

	public void addUserManager(User user) {
		encrypt(user);
		userData.addUserManager(user);
	}

	public void addUserEmployee(User user) {
		encrypt(user);
		userData.addUserEmployee(user);
	}

	public User findUserByID(int id) {
		return userData.findUserByID(id);
	}

	public void deactivateManager(User user) {
		userData.deactivateManager(user);
	}

	public List<User> viewDeactivateUser() {
		return userData.getDeactivateUserManager();
	}

	private void encrypt(User user) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	}

	public void editPrincipal(User user, boolean passwordChanged) {
		if (passwordChanged == true) {
			encrypt(user);
		}
		userData.editPrincipal(user);
	}

}
