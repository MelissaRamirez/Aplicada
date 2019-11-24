package com.venticas.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.venticas.domain.Address;
import com.venticas.domain.ContactInformation;
import com.venticas.domain.Customer;
import com.venticas.domain.User;

@Repository
public class CustomerData {

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

	public List<ContactInformation> getAllContactInformationByCustomerID(int idCustomer) {
		Connection connection = null;
		List<ContactInformation> contactInformationList = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelect = "Select c.ContactInformationID, c.Phone, c.Email" + " from ContactInformation c"
					+ " where  c.CustomerID=" + idCustomer;
			contactInformationList = new LinkedList<>();

			CallableStatement csContactInformation = connection.prepareCall(sqlSelect);
			ResultSet rs = csContactInformation.executeQuery();
			ContactInformation contactInformation = null;
			while (rs.next()) {
				contactInformation = new ContactInformation();
				contactInformation.setId(rs.getInt("ContactInformationID"));
				contactInformation.setEmail(rs.getString("Email"));
				contactInformation.setPhone(rs.getString("Phone"));
				contactInformationList.add(contactInformation);
			}
			connection.commit();

		} catch (

		SQLException e) {
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
		return contactInformationList;

	}

	public List<Address> getAllAddressByCustomerID(int idCustomer) {
		Connection connection = null;
		List<Address> AddressList = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelect = "Select a.AddressID, a.AddressLine, a.City, a.State, a.PostalCode" + " from Address a"
					+ " where a.CustomerID=" + idCustomer;
			AddressList = new LinkedList<>();

			CallableStatement csAddress = connection.prepareCall(sqlSelect);
			ResultSet rs = csAddress.executeQuery();
			Address address = null;
			while (rs.next()) {
				address = new Address();
				address.setId(rs.getInt("AddressID"));
				address.setAddressLine(rs.getString("AddressLine"));
				address.setCity(rs.getString("City"));
				address.setPostalCode(rs.getString("PostalCode"));
				address.setState(rs.getString("State"));
				AddressList.add(address);
			}
			connection.commit();

		} catch (

		SQLException e) {
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
		return AddressList;

	}

	public ContactInformation getAllContactInformation(int idCustomer, int idContact) {
		Connection connection = null;
		ContactInformation contactInformation = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelect = "Select c.ContactInformationID, c.Phone, c.Email" + " from ContactInformation c"
					+ " where  c.CustomerID=" + idCustomer + " AND c.ContactInformationID=" + idContact;

			CallableStatement csContactInformation = connection.prepareCall(sqlSelect);
			ResultSet rs = csContactInformation.executeQuery();
			while (rs.next()) {
				contactInformation = new ContactInformation();
				contactInformation.setId(rs.getInt("ContactInformationID"));
				contactInformation.setEmail(rs.getString("Email"));
				contactInformation.setPhone(rs.getString("Phone"));
			}
			connection.commit();

		} catch (

		SQLException e) {
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
		return contactInformation;

	}

	public Address getAddress(int idCustomer, int addressID) {
		Connection connection = null;
		Address address = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelect = "Select a.AddressID, a.AddressLine, a.City, a.State, a.PostalCode" + " from Address a"
					+ " where a.CustomerID=" + idCustomer + " AND a.AddressID= " + addressID;

			CallableStatement csAddress = connection.prepareCall(sqlSelect);
			ResultSet rs = csAddress.executeQuery();
			while (rs.next()) {
				address = new Address();
				address.setId(rs.getInt("AddressID"));
				address.setAddressLine(rs.getString("AddressLine"));
				address.setCity(rs.getString("City"));
				address.setPostalCode(rs.getString("PostalCode"));
				address.setState(rs.getString("State"));
			}
			connection.commit();

		} catch (

		SQLException e) {
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
		return address;

	}

	// TODO Documentar
	public Customer findCustomerByID(User user) {
		Connection connection = null;
		Customer customer = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlFindEmployee = "SELECT c.CustomerID, IsSubscribed, AddressID, AddressLine, City, State, PostalCode, ContactInformationID,"
					+ " Phone, Email FROM Customer c INNER JOIN Address a ON a.CustomerID =  c.CustomerID INNER JOIN ContactInformation ci ON (ci.CustomerID = c.CustomerID)"
					+ " WHERE (c.CustomerID = ?)";
			CallableStatement csFindEmployee = connection.prepareCall(sqlFindEmployee);
			csFindEmployee.setInt(1, user.getId());
			ResultSet rs = csFindEmployee.executeQuery();
			Map<Integer, Customer> map = new HashMap<Integer, Customer>();
			Map<Integer, Address> addressMap = new HashMap<Integer, Address>();
			Map<Integer, ContactInformation> contactInfoMap = new HashMap<Integer, ContactInformation>();
			while (rs.next()) {
				Address address = null;
				ContactInformation contactInformation = null;
				Integer currentCustomerId = rs.getInt("c.CustomerID");
				Integer currentAddressId = rs.getInt("AddressID");
				Integer currentContactInfoId = rs.getInt("ContactInformationID");
				customer = map.get(currentCustomerId);
				address = addressMap.get(currentAddressId);
				contactInformation = contactInfoMap.get(currentContactInfoId);
				if (customer == null) {
					customer = new Customer();
					customer.setId(rs.getInt("c.CustomerID"));
					customer.setSubscribed(rs.getBoolean("IsSubscribed"));
					map.put(currentCustomerId, customer);
				}

				if (contactInformation == null) {
					contactInformation = new ContactInformation();
					contactInformation.setId(rs.getInt("ContactInformationID"));
					contactInformation.setEmail(rs.getString("Email"));
					contactInformation.setPhone(rs.getString("Phone"));
					customer.getContactInformations().add(contactInformation);
					contactInfoMap.put(currentContactInfoId, contactInformation);
				}
				if (address == null) {
					address = new Address();
					address.setId(rs.getInt("AddressID"));
					address.setAddressLine(rs.getString("AddressLine"));
					address.setCity(rs.getString("City"));
					address.setPostalCode(rs.getString("PostalCode"));
					address.setState(rs.getString("State"));
					customer.getAddress().add(address);
					addressMap.put(currentAddressId, address);
				}

			}

			if (customer == null) {
				String sqlFindCustomer = "SELECT c.CustomerID, IsSubscribed FROM Customer c"
						+ " WHERE (c.CustomerID = ?)";
				CallableStatement csFindCustomer = connection.prepareCall(sqlFindCustomer);
				csFindCustomer.setInt(1, user.getId());
				ResultSet rs2 = csFindCustomer.executeQuery();
				while (rs2.next()) {
					customer = new Customer();
					customer.setId(rs2.getInt("c.CustomerID"));
					customer.setSubscribed(rs2.getBoolean("IsSubscribed"));
				}
			}

			connection.commit();

		} catch (

		SQLException e) {
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
		return customer;
	}

	// TODO Documentar
	public boolean firstLoginCustomer(User user) {
		Connection connection = null;
		boolean firstTime = true;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlFindUser = "SELECT AddressID, ContactInformationID FROM Customer c "
					+ "INNER JOIN ContactInformation ci ON (c.CustomerID = ci.CustomerID) "
					+ "INNER JOIN Address a ON (a.CustomerID = c.CustomerID) WHERE(c.CustomerID = ?)";
			CallableStatement csFindUser = connection.prepareCall(sqlFindUser);
			csFindUser.setInt(1, user.getId());
			ResultSet rs = csFindUser.executeQuery();
			while (rs.next()) {
				if (rs.getString("AddressID") != null && rs.getString("ContactInformationID") != null)
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

	// TODO Documentar
	@Transactional(readOnly = true)
	public void addCustomerDetailsAddress(Customer customer) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlAddCustomerAddress = "{CALL add_customer_address(?, ?, ?, ?, ? ,?)}";
			CallableStatement csAddCustomerAddress = connection.prepareCall(sqlAddCustomerAddress);
			List<Address> addresses = customer.getAddress();
			for (Address address : addresses) {
				csAddCustomerAddress.registerOutParameter(1, java.sql.Types.INTEGER);
				csAddCustomerAddress.setInt(2, customer.getId());
				csAddCustomerAddress.setString(3, address.getAddressLine());
				csAddCustomerAddress.setString(4, address.getCity());
				csAddCustomerAddress.setString(5, address.getState());
				csAddCustomerAddress.setString(6, address.getPostalCode());
				csAddCustomerAddress.executeQuery();
				address.setId(csAddCustomerAddress.getInt(1));
				csAddCustomerAddress.clearParameters();
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

	}

	// TODO Documentar
	@Transactional(readOnly = true)
	public void addCustomerDetailsContactInfo(Customer customer) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlAddCustomerContactInfo = "{CALL add_customer_contactInfo(?, ?, ?, ?)}";
			CallableStatement csAddCustomerContactInfo = connection.prepareCall(sqlAddCustomerContactInfo);
			List<ContactInformation> contactInformations = customer.getContactInformations();
			for (ContactInformation contactInformation : contactInformations) {
				csAddCustomerContactInfo.registerOutParameter(1, java.sql.Types.INTEGER);
				csAddCustomerContactInfo.setInt(2, customer.getId());
				csAddCustomerContactInfo.setString(3, contactInformation.getPhone());
				csAddCustomerContactInfo.setString(4, contactInformation.getEmail());
				csAddCustomerContactInfo.executeQuery();
				contactInformation.setId(csAddCustomerContactInfo.getInt(1));
				csAddCustomerContactInfo.clearParameters();

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

	}

	public void updateCustomerContactInformation(ContactInformation contacti) {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			String sqlUpdateContactInfo = "{CALL update_customerContactInformation(?, ?, ?)}";
			CallableStatement ContactInfo = connection.prepareCall(sqlUpdateContactInfo);

			ContactInfo.setString(1, contacti.getPhone());
			ContactInfo.setString(2, contacti.getEmail());
			ContactInfo.setInt(3, contacti.getId());
			ContactInfo.executeQuery();
			ContactInfo.clearParameters();
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

	public void updateCustomerAddress(Address address) {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			String sqlUpdateAddress = "{CALL update_customerAddress(?, ?, ?, ?, ?)}";
			CallableStatement CustomerAddress = connection.prepareCall(sqlUpdateAddress);

			CustomerAddress.setInt(1, address.getId());
			CustomerAddress.setString(2, address.getAddressLine());
			CustomerAddress.setString(3, address.getCity());
			CustomerAddress.setString(4, address.getState());
			CustomerAddress.setString(5, address.getPostalCode());
			CustomerAddress.executeQuery();
			CustomerAddress.clearParameters();

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

	public void deleteCustomerByID(int id) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String sqlDeleteCustomerProc = "{call delete_customer(?)}";
			CallableStatement csDeleteCustomer = connection.prepareCall(sqlDeleteCustomerProc);
			csDeleteCustomer.setInt(1, id);
			csDeleteCustomer.execute();
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

}