package com.venticas.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.venticas.business.EmployeeBusiness;
import com.venticas.business.UserBusiness;
import com.venticas.domain.User;
import com.venticas.form.UserForm;

@Controller
public class UserController {

	@Autowired
	UserBusiness userBusiness;
	@Autowired
	EmployeeBusiness employeeBusiness;

	@RequestMapping(value = "/Register", method = RequestMethod.GET)
	public String addUserCustomer(Model model) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return "user/registerCustomer";
	}

	@RequestMapping(value = "/Register", method = RequestMethod.POST)
	public String addUserCustomer(@Valid UserForm userForm, BindingResult br, Model model) {
		if(br.hasErrors()) {
			return "user/registerCustomer";
		} else {
			User user = new User();
			BeanUtils.copyProperties(userForm, user);
			userBusiness.addUserCustomer(user);
			return "login";
		}
	}

	@RequestMapping(value = "/User", method = RequestMethod.GET)
	public String viewUserManager(Model model) {
		return "user/User";
	}

	@RequestMapping(value = "/Manager", method = RequestMethod.GET)
	public String viewManagers(Model model) {
		List<User> users = userBusiness.getUserManager();
		model.addAttribute("users", users);
		return "manager/manager";
	}

	@RequestMapping(value = "/Employee", method = RequestMethod.GET)
	public String viewEmployees(Model model) {
		List<User> users = userBusiness.getUserEmployees();
		model.addAttribute("users", users);
		return "user/employee";
	}

	@RequestMapping(value = "/Client", method = RequestMethod.GET)
	public String viewClients(Model model) {
		List<User> users = userBusiness.getUserCustomer();
		model.addAttribute("users", users);
		return "user/client";
	}

	@RequestMapping(value = "/AddManager", method = RequestMethod.GET)
	public String addManager(Model model) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return "manager/addManager";
	}

	@RequestMapping(value = "/AddManager", method = RequestMethod.POST)
	public String addManager(@Valid UserForm userForm, BindingResult br, Model model) {
		if(br.hasErrors()) {
			return "manager/addManager";
		} else {
			User user = new User();
			BeanUtils.copyProperties(userForm, user);
			userBusiness.addUserManager(user);
			return "redirect:/Manager";
		}
	}

	@RequestMapping(value = "/DeactivateManager", method = RequestMethod.POST)
	public String deactivateManager(Model model, @RequestParam("id") int id) {
		User user = userBusiness.findUserByID(id);
		UserForm userForm = new UserForm();
		userForm.setId(user.getId());
		userForm.setIdentification(user.getIdentification());
		userForm.setFirstName(user.getFirstName());
		userForm.setLastName(user.getLastName());
		userForm.setUserName(user.getUserName());
		model.addAttribute("userForm", userForm);
		return "user/deactivateUser";
	}

	@RequestMapping(value = "/DeactivateManagerConfirm", method = RequestMethod.POST)
	public String deactivateManagerConfirm(Model model, UserForm userForm) {
		User user = userBusiness.findUserByID(userForm.getId());
		userBusiness.deactivateManager(user);
		return "redirect:/Manager";
	}

	@RequestMapping(value = "/DeactivateUser", method = RequestMethod.GET)
	public String viewDeactivateUser(Model model) {
		List<User> users = userBusiness.viewDeactivateUser();
		model.addAttribute("users", users);
		return "user/deactivated";
	}

	@RequestMapping(value = "/AddEmployee", method = RequestMethod.GET)
	public String addEmployee(Model model) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return "user/addEmployee";
	}

	@RequestMapping(value = "/AddEmployee", method = RequestMethod.POST)
	public String addEmployee(@Valid UserForm userForm, BindingResult br, Model model) {
		if(br.hasErrors()) {
			return "user/addEmployee";
		} else {
			User user = new User();
			BeanUtils.copyProperties(userForm, user);
			userBusiness.addUserEmployee(user);
			return "redirect:/Employee";
		}
	}

	@RequestMapping(value = "/Principal", method = RequestMethod.GET)
	public String viewPrincipal(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		UserForm userForm = new UserForm();
		userForm.setId(user.getId());
		userForm.setFirstName(user.getFirstName());
		userForm.setLastName(user.getLastName());
		userForm.setIdentification(user.getIdentification());
		userForm.setUserName(user.getUserName());
		model.addAttribute("userForm", userForm);

		return "user/editPrincipal";
	}

	@RequestMapping(value = "/Principal", method = RequestMethod.POST)
	public String editPrincipal(Model model, UserForm userForm, HttpSession session) {
		boolean passwordChanged = false;
		User user = userBusiness.findUserByID(userForm.getId());
		user.setFirstName(userForm.getFirstName());
		user.setLastName(userForm.getLastName());
		user.setIdentification(userForm.getIdentification());
		user.setUserName(userForm.getUserName());
		if (!userForm.getPassword().equals("")) {
			user.setPassword(userForm.getPassword());
			passwordChanged = true;
		}
		userBusiness.editPrincipal(user, passwordChanged);
		user = userBusiness.findUserByID(userForm.getId());
		session.setAttribute("user", user);
		return "user/editPrincipal";
	}
	
	@RequestMapping(value = "/Family-Manager", method = RequestMethod.GET)
	public String family() {
		return "family-manager";
	}

}
