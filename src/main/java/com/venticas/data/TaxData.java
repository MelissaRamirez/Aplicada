/*
 * Clase Trabajada por Luis
 */
package com.venticas.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.venticas.domain.Tax;

// TODO: Auto-generated Javadoc
/**
 * The Class TaxData.
 * Esta clase se encarga del Control de los datos de Impuestos
 */
@Repository
public class TaxData {

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
	 * Gets the all taxes.
	 *<p>
	 *Este método se encarga de buscar todos los impuestos en la base de datos
	 *</p>
	 * @return List con todos los impuestos en la base de datos
	 */
	@Transactional(readOnly = true)
	public List<Tax> getAllTaxes() {
		Connection connection = null;
		List<Tax> taxes = new LinkedList<Tax>();

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectGetAllTaxes = "SELECT TaxID, Type, Percentage FROM Tax";
			CallableStatement csProcedureGetAllTaxes = connection.prepareCall(sqlSelectGetAllTaxes);
			ResultSet rs = csProcedureGetAllTaxes.executeQuery();
			while (rs.next()) {
				Tax tax = new Tax();
				tax.setId(rs.getInt("TaxID"));
				tax.setType(rs.getString("Type"));
				tax.setPercentage((rs.getFloat("Percentage")));
				taxes.add(tax);
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

		return taxes;
	}

	/**
	 * Adds the tax.
	 *<p>
	 *Este método se encarga de agregar un impuesto a la base de datos
	 *</p>
	 * @param tax Tax, el impuesto que se desea agregar
	 */
	@Transactional(readOnly = true)
	public void addTax(Tax tax) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlInsertAddTax = "{CALL add_tax(?, ?, ?)}";
			CallableStatement csInsertAddTax = connection.prepareCall(sqlInsertAddTax);
			csInsertAddTax.setString(2, tax.getType());
			csInsertAddTax.setFloat(3, tax.getPercentage());
			csInsertAddTax.executeQuery();
			csInsertAddTax.registerOutParameter(1, java.sql.Types.INTEGER);
			tax.setId(csInsertAddTax.getInt(1));

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

	/**
	 * Update tax.
	 *<p>
	 *Este método se encarga de ir a la base de datos y actualizar un impuesto en la base de datos
	 *</p>
	 * @param tax Tax, es el impuesto uese va a actulizar
	 */
	@Transactional(readOnly = true)
	public void updateTax(Tax tax) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlUpdateTax = "UPDATE Tax SET Type= ?, Percentage= ? WHERE TaxID = ?";
			PreparedStatement psUpdateTax = connection.prepareStatement(sqlUpdateTax);
			psUpdateTax.setString(1, tax.getType());
			psUpdateTax.setFloat(2, tax.getPercentage());
			psUpdateTax.setInt(3, tax.getId());
			psUpdateTax.executeUpdate();

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

	/**
	 * Delete tax.
	 *<p>
	 *Este método se encarga de eliminar un impuesto de la base de datos
	 *</p>
	 * @param idTax int, el id del impuesto que se desea eliminar
	 */
	@Transactional(readOnly = true)
	public void deleteTax(int idTax) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlDeleteTax = "DELETE FROM Tax WHERE TaxID = ?";
			PreparedStatement paDeleteTax = connection.prepareStatement(sqlDeleteTax);
			paDeleteTax.setInt(1, idTax);
			paDeleteTax.executeUpdate();

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

	/**
	 * Find tax by type.
	 *<p>
	 *Este método se encarga de ir a la base de datos y buscar un impuesto por medio del Tipo
	 *</p>
	 * @param taxType String, tipo de impuesto
	 * @return the tax, el impuesto encontrado o nulo en caso de que no lo encuentre
	 */
	public Tax findTaxByType(String taxType) {
		Connection connection = null;
		Tax tax = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectGetTaxByType = "SELECT TaxID, Type, Percentage FROM Tax WHERE Type= ?";
			PreparedStatement paSelectGetTaxByType = connection.prepareStatement(sqlSelectGetTaxByType);
			paSelectGetTaxByType.setString(1, taxType);
			ResultSet rs = paSelectGetTaxByType.executeQuery();
			while (rs.next()) {
				tax = new Tax();
				tax.setId(rs.getInt("TaxID"));
				tax.setType(rs.getString("Type"));
				tax.setPercentage((rs.getFloat("Percentage")));
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

		return tax;
	}
	
	
	/**
	 * Find tax by ID.
	 *<p>
	 *Este método se encarga de ir a la base de datos y buscar el impuesto por medio de ID
	 *</p>
	 * @param taxID int, ID de impuesto
	 * @return the tax, el impuesto encontrado o nulo en caso de que no lo encuentre
	 */
	public Tax findTaxByID(int taxID) {
		Connection connection = null;
		Tax tax = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectGetTaxByType = "SELECT TaxID, Type, Percentage FROM Tax WHERE TaxID= ?";
			PreparedStatement paSelectGetTaxByType = connection.prepareStatement(sqlSelectGetTaxByType);
			paSelectGetTaxByType.setInt(1, taxID);
			ResultSet rs = paSelectGetTaxByType.executeQuery();
			while (rs.next()) {
				tax = new Tax();
				tax.setId(rs.getInt("TaxID"));
				tax.setType(rs.getString("Type"));
				tax.setPercentage((rs.getFloat("Percentage")));
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

		return tax;
	}
}
