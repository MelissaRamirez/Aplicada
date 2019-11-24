package com.venticas.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venticas.data.EmployeeData;
import com.venticas.domain.Employee;
import com.venticas.domain.User;

@Service
public class EmployeeBusiness {

	@Autowired
	EmployeeData employeeData;

	// TODO Documentar
	public boolean firstLoginEployee(User user) {
		return employeeData.firstLoginEmployee(user);
	}
	
	//TODO Documentar
	public void addEmployeeDetails(Employee employee) {
		employeeData.addEmployeeDetails(employee);
	}
	
	// TODO Documentar
		public Employee findEmployeeByID(User user) {
			return employeeData.findEmployeeByID(user);
		}

}
