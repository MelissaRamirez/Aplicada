package com.venticas.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.venticas.business.EmployeeBusiness;
import com.venticas.domain.Employee;
import com.venticas.domain.User;
import com.venticas.form.EmployeeForm;

@Controller
public class EmployeeController {

	@Autowired
	EmployeeBusiness employeeBusiness;

	@RequestMapping(value = "AddEmployeeDetails", method = RequestMethod.GET)
	public String addEmployeeDetails(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		EmployeeForm employeeForm = new EmployeeForm();
		employeeForm.setId(user.getId());
		model.addAttribute("employeeForm", employeeForm);
		return "user/employeeDetail";
	}
	
	@RequestMapping(value = "AddEmployeeDetails", method = RequestMethod.POST)
	public String addEmployeeDetails(@Valid EmployeeForm employeeForm, BindingResult br, Model model) {
		if(br.hasErrors()) {
			return "user/addEmployee";
		} else {
			Employee employee = new Employee();
			BeanUtils.copyProperties(employeeForm, employee);
			employeeBusiness.addEmployeeDetails(employee);
			return "redirect:/home";
		}
	}
	
	@RequestMapping(value = "/EmployeeDetailsEdit", method = RequestMethod.GET)
	public String viewPersonalDeatils(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Employee employee = employeeBusiness.findEmployeeByID(user);
		EmployeeForm employeeForm = new EmployeeForm();
		employeeForm.setId(employee.getId());
		employeeForm.setEmail(employee.getEmail());
		employeeForm.setPhone(employee.getPhone());
		model.addAttribute("employeeForm", employeeForm);
		return "user/employeeDetailEdit";

	}

	@RequestMapping(value = "EmployeeDetailsEdit", method = RequestMethod.POST)
	public String editEmployeeDetails(Model model, EmployeeForm employeeForm) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeForm, employee);
		employeeBusiness.addEmployeeDetails(employee);
		return "redirect:/EmployeeDetailsEdit";
	}
}
