/*
 * Trabajado por Luis
 */
package com.venticas.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.venticas.domain.Role;
import com.venticas.domain.User;

//
/**
 * The Class UserData.
 */
@Repository
public class UserData {

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

	/**
	 * Este método se encarga de ir a buscar un usuario por medio del username a la
	 * base
	 *
	 * @param userName El username del usuario
	 * @return user, el usuario encontrado en la base
	 */
	public User findUserByUserName(String userName) {
		Connection connection = null;
		User user = new User();
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, LastName, " + "UserName, "
					+ "Password, " + "r.RoleID, " + "TypeName " + "FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) " + "WHERE (UserName = ?)";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			csLoginCustomer.setString(1, userName);
			ResultSet rs = csLoginCustomer.executeQuery();
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
			}
			System.out.println("Usuario" + user.getPassword() + user.getUserName() + user.getRole().getId());
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
		return user;
	}

	// TODO Documentar
	@Transactional(readOnly = true)
	public void addUserCustomer(User user) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			System.out.println("Usuario" + user.getPassword() + user.getUserName() + user.getRole());
			
			String sqlInsertAddUser = "{CALL add_user_customer(?, ?, ?, ?, ? ,?)}";
			CallableStatement csInsertAddUser = connection.prepareCall(sqlInsertAddUser);
			csInsertAddUser.setString(1, user.getIdentification());
			csInsertAddUser.setString(2, user.getFirstName());
			csInsertAddUser.setString(3, user.getLastName());
			csInsertAddUser.setString(4, user.getUserName());
			csInsertAddUser.setString(5, user.getPassword());
			csInsertAddUser.registerOutParameter(6, java.sql.Types.INTEGER);
			csInsertAddUser.executeQuery();
			user.setId(csInsertAddUser.getInt(6));

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
	}

	// TODO Documentar
	public List<User> getUserManager() {
		Connection connection = null;
		List<User> users = null;
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, " + "LastName, "
					+ "UserName, " + "Password, " + "r.RoleID, " + " TypeName," + " Active FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) "
					+ "WHERE (TypeName = 'Administrador' && Active = 1) ";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			ResultSet rs = csLoginCustomer.executeQuery();
			users = new LinkedList<User>();
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
				users.add(user);
			}

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
		return users;
	}

	// TODO Documentar
	public List<User> getDeactivateUserManager() {
		Connection connection = null;
		List<User> users = null;
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, " + "LastName, "
					+ "UserName, " + "Password, " + "r.RoleID, " + " TypeName," + "Active FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) "
					+ "WHERE (TypeName = 'Administrador' && Active = 0) ";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			ResultSet rs = csLoginCustomer.executeQuery();
			users = new LinkedList<User>();
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
				users.add(user);
			}

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
		return users;
	}

	// TODO Documentar
	public List<User> getUserEmployee() {
		Connection connection = null;
		List<User> users = null;
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, " + "LastName, "
					+ "UserName, " + "Password, " + "r.RoleID, " + "TypeName " + "FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) " + "WHERE (TypeName = 'Empleado') ";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			ResultSet rs = csLoginCustomer.executeQuery();
			users = new LinkedList<User>();
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
				users.add(user);
			}

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
		return users;
	}

	// TODO Documentar
	public List<User> getOrganization() {
		Connection connection = null;
		List<User> users = null;
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, " + "LastName, "
					+ "UserName, " + "Password, " + "r.RoleID, " + "TypeName " + "FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) "
					+ "WHERE (TypeName = 'Administrador' || TypeName = 'Administrador') ";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			ResultSet rs = csLoginCustomer.executeQuery();
			users = new LinkedList<User>();
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
				users.add(user);
			}

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
		return users;
	}

	// TODO Documentar
	public List<User> getCustomer() {
		Connection connection = null;
		List<User> users = null;
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, " + "LastName, "
					+ "UserName, " + "Password, " + "r.RoleID, " + "TypeName " + "FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) " + "WHERE (TypeName = 'Cliente') ";
			
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			ResultSet rs = csLoginCustomer.executeQuery();
			
			users = new LinkedList<User>();
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
				
				
				
				users.add(user);
			}

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
		return users;
	}

	// TODO Documentar
	public void addUserManager(User user) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlInsertAddUser = "{CALL add_user_manager(?, ?, ?, ?, ? ,?)}";
			CallableStatement csInsertAddUser = connection.prepareCall(sqlInsertAddUser);
			csInsertAddUser.setString(1, user.getIdentification());
			csInsertAddUser.setString(2, user.getFirstName());
			csInsertAddUser.setString(3, user.getLastName());
			csInsertAddUser.setString(4, user.getUserName());
			csInsertAddUser.setString(5, user.getPassword());
			csInsertAddUser.registerOutParameter(6, java.sql.Types.INTEGER);
			csInsertAddUser.executeQuery();
			user.setId(csInsertAddUser.getInt(6));

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

	}

	// TODO Documentar
	@Transactional(readOnly = true)
	public void addUserEmployee(User user) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlInsertAddUser = "{CALL add_user_employee(?, ?, ?, ?, ? ,?)}";
			CallableStatement csInsertAddUser = connection.prepareCall(sqlInsertAddUser);
			csInsertAddUser.setString(1, user.getIdentification());
			csInsertAddUser.setString(2, user.getFirstName());
			csInsertAddUser.setString(3, user.getLastName());
			csInsertAddUser.setString(4, user.getUserName());
			csInsertAddUser.setString(5, user.getPassword());
			csInsertAddUser.registerOutParameter(6, java.sql.Types.INTEGER);
			csInsertAddUser.executeQuery();
			user.setId(csInsertAddUser.getInt(6));

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

	}

	// TODO Documentar
	public User findUserByID(int id) {
		Connection connection = null;
		User user = new User();
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "SELECT UserID, " + "Identification, " + "FirstName, LastName, " + "UserName, "
					+ "Password, " + "r.RoleID, " + "TypeName " + "FROM User u "
					+ "INNER JOIN Role r ON (u.RoleID = r.RoleID) " + "WHERE (UserID = ?)";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			csLoginCustomer.setInt(1, id);
			ResultSet rs = csLoginCustomer.executeQuery();
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("r.RoleID"));
				role.setTypeName(rs.getString("TypeName"));
				user.setRole(role);
				user.setId(rs.getInt("UserID"));
				user.setIdentification(rs.getString("Identification"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setUserName(rs.getString("UserName"));
				user.setPassword(rs.getString("Password"));
			}

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
		return user;
	}

	@Transactional(readOnly = true)
	public void deactivateManager(User user) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String selectLoginCustomer = "UPDATE User SET Active = 0 WHERE (UserID = ?);";
			CallableStatement csLoginCustomer = connection.prepareCall(selectLoginCustomer);
			csLoginCustomer.setInt(1, user.getId());
			csLoginCustomer.executeUpdate();
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

	// TODO Documentar
	@Transactional(readOnly = true)
	public void editPrincipal(User user) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String updatePrincipal = "UPDATE User SET Identification = ?, FirstName = ?, LastName= ?, "
					+ "UserName = ?, Password = ? WHERE (UserID = ?);";
			CallableStatement csUpdatePrincipal = connection.prepareCall(updatePrincipal);
			csUpdatePrincipal.setString(1, user.getIdentification());
			csUpdatePrincipal.setString(2, user.getFirstName());
			csUpdatePrincipal.setString(3, user.getLastName());
			csUpdatePrincipal.setString(4, user.getUserName());
			csUpdatePrincipal.setString(5, user.getPassword());
			csUpdatePrincipal.setInt(6, user.getId());
			csUpdatePrincipal.executeUpdate();
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

}
