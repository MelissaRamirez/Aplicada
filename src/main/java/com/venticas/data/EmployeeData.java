package com.venticas.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.venticas.domain.Employee;
import com.venticas.domain.User;

@Repository
public class EmployeeData {

	@Autowired
	UserData userData;

	/** The data source. */
	@Autowired
	private DataSource dataSource;

	/**
	 * Sets the data source.
	 *
	 * @param dataSource the new data source
	 */
	@Autowired
	private void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// TODO Documentar
	public boolean firstLoginEmployee(User user) {
		Connection connection = null;
		boolean firstTime = true;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlFindUser = "SELECT Email FROM Employee WHERE (EmployeeID = ?)";
			CallableStatement csFindUser = connection.prepareCall(sqlFindUser);
			csFindUser.setInt(1, user.getId());
			ResultSet rs = csFindUser.executeQuery();
			while (rs.next()) {
				if (rs.getString("Email") != null)
					firstTime = false;
			}

			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return firstTime;
	}

	@Transactional(readOnly = true)
	public void addEmployeeDetails(Employee employee) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String addEmployeeDetails = "UPDATE Employee SET Email = ?, Phone = ? WHERE (EmployeeID = ?);";
			CallableStatement csAddEmployeeDetails = connection.prepareCall(addEmployeeDetails);
			csAddEmployeeDetails.setString(1, employee.getEmail());
			csAddEmployeeDetails.setString(2, employee.getPhone());
			csAddEmployeeDetails.setInt(3, employee.getId());
			csAddEmployeeDetails.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

	
	//TODO Documentar
	public Employee findEmployeeByID(User user) {
		Connection connection = null;
		Employee employee = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlFindEmployee = "SELECT EmployeeID, Email, Phone FROM Employee WHERE (EmployeeID = ?)";
			CallableStatement csFindEmployee = connection.prepareCall(sqlFindEmployee);
			csFindEmployee.setInt(1, user.getId());
			ResultSet rs = csFindEmployee.executeQuery();
			while (rs.next()) {
				employee = new Employee();
				employee.setId(rs.getInt("EmployeeID"));
				employee.setEmail(rs.getString("Email"));
				employee.setPhone(rs.getString("Phone"));
			}

			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return employee;
	}

}
