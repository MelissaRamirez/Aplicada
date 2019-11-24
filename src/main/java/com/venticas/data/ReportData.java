package com.venticas.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.venticas.domain.ReportItem;
import com.venticas.domain.User;

@Repository
public class ReportData {

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

	public List<ReportItem> getBestClient() {
		Connection conexion = null;
		List<ReportItem> items = null;
		try {
			conexion = dataSource.getConnection();
			conexion.setAutoCommit(false);
			String sqlSelect = "SELECT DISTINCT o.CustomerID,FirstName, LastName, count(o.CustomerID) AS 'numOrders' FROM Orders o "
					+ "INNER JOIN Customer c on (o.CustomerID = c.CustomerID) INNER JOIN User on (UserID = c.CustomerID) "
					+ "ORDER BY ('numOrders') ASC LIMIT 10";

			PreparedStatement selectTop10Customers = conexion.prepareStatement(sqlSelect);
			ResultSet rs = selectTop10Customers.executeQuery();
			ReportItem report = null;
			items = new LinkedList<ReportItem>();
			while (rs.next()) {
				report = new ReportItem();
				User user = new User();
				user.setId(rs.getInt("o.CustomerID"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName("LastName");
				report.setUser(user);
				report.setNumOrders(rs.getInt("numOrders"));
				items.add(report);
			}
		} catch (SQLException e) {
			try {
				conexion.rollback(); // por si el rollback no se ejecuta correractemente
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}

			throw new RuntimeException(e);
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return items;

	}
}
