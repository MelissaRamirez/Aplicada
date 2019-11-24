/*
 * Trabajada por Luis
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

import com.venticas.domain.ProductCategory;

/**
 * The Class ProductCategoryData.
 * <p>
 * Esta clase se encargará del acceso a datos de ProductCategory
 * </p>
 */
@Repository
public class ProductCategoryData {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the categories.
	 * <p>
	 * Este método trae todos los productos de la base de datos
	 * </P>
	 * 
	 * @return lista de ProductCategory
	 */
	@Transactional(readOnly = true)
	public List<ProductCategory> getCategories() {
		Connection connection = null;
		List<ProductCategory> categories = new LinkedList<ProductCategory>();

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectGetAllCategory = "SELECT ProductCategoryID, ProductCategoryName, TaxID FROM ProductCategory";
			CallableStatement csSelectGetAllCategory = connection.prepareCall(sqlSelectGetAllCategory);
			ResultSet rs = csSelectGetAllCategory.executeQuery();
			while(rs.next()) {
				ProductCategory productCategory = new ProductCategory();
				productCategory.setId(rs.getInt("ProductCategoryID"));
				productCategory.setName(rs.getString("ProductCategoryName"));
				productCategory.getTax().setId(rs.getInt("TaxID"));
				categories.add(productCategory);
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
		
		return categories;
	}

	/**
	 * Adds the category.
	 * <p>
	 * Este método se encarga de agregar una categoría a la base de datos
	 * </p>
	 * 
	 * @param ProductCategory category, catgoría que se desea agregar
	 */
	@Transactional(readOnly = true)
	public void addCategory(ProductCategory category) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlInsertAddCategory = "{CALL add_category(?, ?, ?)}";
			CallableStatement csInsertAddCategory = connection.prepareCall(sqlInsertAddCategory);
			csInsertAddCategory.registerOutParameter(1, java.sql.Types.INTEGER);
			csInsertAddCategory.setString(2, category.getName());
			csInsertAddCategory.setInt(3, category.getTax().getId());
			csInsertAddCategory.executeQuery();
			category.setId(csInsertAddCategory.getInt(1));

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
	 * Update category.
	 * <p>
	 * Este método se encarga de editar una categoría en la base de datos
	 * </p>
	 * 
	 * @param ProductCategory category, categoría que se desea editar
	 */
	@Transactional(readOnly = true)
	public void updateCategory(ProductCategory category) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlUpdateAddCategory = "UPDATE ProductCategory SET ProductCategoryName= ?, TaxID= ? WHERE ProductCategoryID = ?";
			PreparedStatement paUpdateAddCategory = connection.prepareStatement(sqlUpdateAddCategory);
			paUpdateAddCategory.setString(1, category.getName());
			paUpdateAddCategory.setInt(2, category.getTax().getId());
			paUpdateAddCategory.setInt(3, category.getId());
			paUpdateAddCategory.executeUpdate();

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
	 * Delete category.
	 * <p>
	 * Este método se encarga de eliminar una categoría de la base de datos
	 * </p>
	 * 
	 * @param category the category
	 */
	@Transactional(readOnly = true)
	public void deleteCategory(int idCategory) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlDeleteCategory = "DELETE FROM ProductCategory WHERE ProductCategoryID = ?";
			PreparedStatement paDeleteCategory = connection.prepareStatement(sqlDeleteCategory);
			paDeleteCategory.setInt(1, idCategory);
			paDeleteCategory.executeUpdate();

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
	 * Find category by name.
	 * <p>
	 * Este método se encarga de buscar una categoría por su nombre
	 * </p>
	 * 
	 * @param String name, nombre de la categoría
	 * @return the product, categoría buscada
	 */
	@Transactional(readOnly = true)
	public ProductCategory findCategoryByName(String name) {
		Connection connection = null;
		ProductCategory productCategory = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectFindCategory = "SELECT ProductCategoryID, ProductCategoryName, TaxID FROM ProductCategory WHERE ProductCategoryName = ?";
			PreparedStatement paSelectFindCategory = connection.prepareStatement(sqlSelectFindCategory);
			paSelectFindCategory.setString(1, name);
			ResultSet rs = paSelectFindCategory.executeQuery();
			while(rs.next()) {
				productCategory = new ProductCategory();
				productCategory.setId(rs.getInt("ProductCategoryID"));
				productCategory.setName(rs.getString("ProductCategoryName"));
				productCategory.getTax().setId(rs.getInt("TaxID"));
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
		
		return productCategory;

	}

	//TODO Documentar
	public ProductCategory findCategoryByID(int id) {
		Connection connection = null;
		ProductCategory productCategory = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectFindCategory = "SELECT ProductCategoryID, ProductCategoryName, TaxID FROM ProductCategory WHERE ProductCategoryID = ?";
			PreparedStatement paSelectFindCategory = connection.prepareStatement(sqlSelectFindCategory);
			paSelectFindCategory.setInt(1, id);
			ResultSet rs = paSelectFindCategory.executeQuery();
			while(rs.next()) {
				productCategory = new ProductCategory();
				productCategory.setId(rs.getInt("ProductCategoryID"));
				productCategory.setName(rs.getString("ProductCategoryName"));
				productCategory.getTax().setId(rs.getInt("TaxID"));
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
		
		return productCategory;
	}
}
