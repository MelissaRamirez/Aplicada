/*
 *Trabajado por Luis 
 */
package com.venticas.controller;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.venticas.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.venticas.domain.User;

@Controller
public class HomeController {

	@Autowired
	EmployeeBusiness employeeBusiness;
	@Autowired
	UserBusiness userBusiness;
	@Autowired
	CustomerBusiness customerBusiness;
	@Autowired
	private CategoryBusiness categoryBusiness;
	@Autowired
	private FamilyBusiness familyBusiness;

	/**
	 * Login. Este método se utiliza para loguearse a la aplicación
	 * 
	 * @return La página a la que se ha logueado
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, HttpSession session) {
		return "login";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String login(HttpSession session, Model model) {
		// Routing - enrutamiento
		Collection<? extends GrantedAuthority> authorities;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		authorities = auth.getAuthorities();
		boolean isCustomer = false;
		boolean isAdmin = false;
		boolean isEmployee = false;

		model.addAttribute("categories", categoryBusiness.findAll());
		model.addAttribute("families", familyBusiness.findAll());

		for (GrantedAuthority grantedAuthority : authorities) {
			System.out.println(" granted " + grantedAuthority.getAuthority());
			
			if (grantedAuthority.getAuthority().equals("Cliente")) {
				isCustomer = true;
			} else if (grantedAuthority.getAuthority().equals("Empleado")) {
				isEmployee = true;
			} else if (grantedAuthority.getAuthority().equals("Administrador")) {
				isAdmin = true;
			}
		}

		User user = userBusiness.findUserByUserName(auth.getName());
		session.setAttribute("user", user);
		if (isCustomer)
			if(customerBusiness.firstLoginCustomer(user)) {
				return "redirect:/AddCustomerDetails";
			}
			else {
				
				return "home";
			}
		else if (isAdmin)
			return "manager/homeManager";
		else if (isEmployee)
			if (employeeBusiness.firstLoginEployee(user)) {
				return "redirect:/AddEmployeeDetails";
			} else
				return "manager/homeManager";

		throw new IllegalStateException();
	}

	@RequestMapping(value = "logOut", method = RequestMethod.GET)
	public String logOut(Model model, HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/logout";
	}
}
