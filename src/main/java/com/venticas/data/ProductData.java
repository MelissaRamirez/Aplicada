package com.venticas.data;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.venticas.domain.Family;
import com.venticas.domain.Product;
import com.venticas.domain.ProductCategory;
import com.venticas.domain.ProductImage;

@Repository
public class ProductData {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private FamilyData familyData;
	@Autowired
	private ProductImageData imageData;

	public List<Product> findProductByFamily(int familyID) {
		String sqlSelect = "SELECT p.ProductID, ProductName, Description, Price, UnistOnStock, pc.ProductCategoryID, ProductCategoryName"
				+ " FROM Product p JOIN  Family_Product fp ON p.ProductID = fp.ProductID JOIN ProductCategory pc ON p.ProductCategoryID = pc.ProductCategoryID"
				+ " WHERE FamilyID = " + familyID;
		return jdbcTemplate.query(sqlSelect, new ProductsExtractor());
	}

	public List<Product> findAllProducts() {
		String sqlSelect = "SELECT ProductID, ProductName, Description, Price, UnistOnStock, pc.ProductCategoryID, ProductCategoryName"
				+ " FROM Product p JOIN ProductCategory pc ON p.ProductCategoryID = pc.ProductCategoryID;";
		return jdbcTemplate.query(sqlSelect, new ProductsExtractor());
	}

	/*public boolean updateProduct(Product product) {
		Connection connection = null;
		boolean isSuccess = true;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			String sqlProcedure = "{call update_product(?,?,?,?,?,?)}";
			CallableStatement csUpdateProduct = connection.prepareCall(sqlProcedure);
			csUpdateProduct.setString("name", product.getName());
			csUpdateProduct.setString("description", product.getDescription());
			csUpdateProduct.setFloat("price", product.getPrice());
			csUpdateProduct.setInt("units", product.getUnitsOnStock());
			csUpdateProduct.setInt("categoryID", product.getCategory().getId());
			csUpdateProduct.setInt("id", product.getId());
			csUpdateProduct.execute();

			// Se limpia el registro de familias asociado a este producto
			String sqlDeleteFamilies = "DELETE FROM Family_Product" + " WHERE ProductID = " + product.getId();
			CallableStatement sqlDelete = connection.prepareCall(sqlDeleteFamilies);
			sqlDelete.execute();

			// Se guardan las nuevas familias con las que se relaciona este producto
			String sqlSaveProductFamily = "{call save_productFamily(?,?)}";
			CallableStatement csSaveProductFamily[] = new CallableStatement[product.getFamilies().size()];

			for (int i = 0; i < csSaveProductFamily.length; i++) {
				csSaveProductFamily[i] = connection.prepareCall(sqlSaveProductFamily);
				csSaveProductFamily[i].setInt("productID", product.getId());
				csSaveProductFamily[i].setInt("familyID", product.getFamilies().get(i).getId());
				csSaveProductFamily[i].execute();
			} // fin for

			// Se borran las imagenes asociadas con el producto
			String sqlDeleteImages = "DELETE FROM ProductImage" + " WHERE ProductID = " + product.getId();
			sqlDelete = connection.prepareCall(sqlDeleteImages);
			sqlDelete.execute();

			// Se procede a eliminar las imagenes del servidor
			imageData.deleteImages(product.getId());

			// Se ejecuta el procedimiento almacenado para guardar las rutas de la imagenes
			// en la base de datos
			String sqlSaveProductImages = "{call save_productImage(?,?)}";
			CallableStatement csSaveProductImages[] = new CallableStatement[product.getImagenes().size()];

			for (int i = 0; i < csSaveProductImages.length; i++) {
				csSaveProductImages[i] = connection.prepareCall(sqlSaveProductImages);
				csSaveProductImages[i].setInt("productID", product.getId());
				csSaveProductImages[i].setString("filePath", product.getImagenes().get(i).getFilePath());
				csSaveProductImages[i].execute();
			} // fin for

			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return isSuccess;
	}*/

	public Product findProductById(int productID) {

		// Obtiene información básica del producto y su categoria
		String sqlSelect = "SELECT ProductID, ProductName, Description, Price, UnistOnStock, pc.ProductCategoryID, ProductCategoryName"
				+ " FROM Product p JOIN ProductCategory pc ON p.ProductCategoryID = pc.ProductCategoryID"
				+ " WHERE p.ProductID = " + productID;

		Product product = jdbcTemplate.query(sqlSelect, new ProductsExtractor()).get(0);

		// Obtiene las familias del producto
		List<Family> families = familyData.findFamilyByProductId(productID);
		product.setFamilies(families);

		// Obtiene la lista de imagenes del producto
		List<ProductImage> images = imageData.getProductImages(productID);
		product.setImagenes(images);

		return product;
	}

	public void insertProduct(Product product) {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			// Se ejecuta el procedimiento almacenado para guardar en la tabla Product
			String sqlSaveProduct = "{call save_product(?,?,?,?,?,?)}";
			CallableStatement csSaveProduct = connection.prepareCall(sqlSaveProduct);
			csSaveProduct.setString("productName", product.getName());
			csSaveProduct.setString("productDescription", product.getDescription());
			csSaveProduct.setFloat("productPrice", product.getPrice());
			csSaveProduct.setInt("productUnits", product.getUnitsOnStock());
			csSaveProduct.setInt("categoryID", product.getCategory().getId());
			// csSaveProduct.registerOutParameter(6, Types.INTEGER);
			csSaveProduct.execute();
			int productID = csSaveProduct.getInt("productID");

			// Se ejecuta el procedimiento almacenado para guardar en la tabla ProductImage
			String sqlSaveProductImages = "{call save_productImage(?,?)}";
			CallableStatement csSaveProductImages[] = new CallableStatement[product.getImagenes().size()];

			for (int i = 0; i < csSaveProductImages.length; i++) {
				csSaveProductImages[i] = connection.prepareCall(sqlSaveProductImages);
				csSaveProductImages[i].setInt("productID", productID);
				csSaveProductImages[i].setString("filePath", product.getImagenes().get(i).getFilePath());
				csSaveProductImages[i].execute();
			} // fin for

			// Se guardan las familias con las que se relaciona este producto
			String sqlSaveProductFamily = "{call save_productFamily(?,?)}";
			CallableStatement csSaveProductFamily[] = new CallableStatement[product.getFamilies().size()];

			for (int i = 0; i < csSaveProductFamily.length; i++) {
				csSaveProductFamily[i] = connection.prepareCall(sqlSaveProductFamily);
				csSaveProductFamily[i].setInt("productID", productID);
				csSaveProductFamily[i].setInt("familyID", product.getFamilies().get(i).getId());
				csSaveProductFamily[i].execute();
			} // fin for

			// Finalmente se hace commit de la transacción
			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		} finally { // se finaliza la conexion
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}// fin insertProduct

	public boolean deleteProduct(int productID) {
		Connection connection = null;
		boolean deleteConfirmation = true;
		try {
			connection = dataSource.getConnection();

			// Se procede a eliminar las imagenes del servidor
			imageData.deleteImages(productID);

			// Se ejecuta procedimiento almacenado para eliminar un producto
			String sqlProcedure = "{call delete_product(?)}";
			CallableStatement csDeleteProduct = connection.prepareCall(sqlProcedure);
			csDeleteProduct.setInt("id", productID);
			csDeleteProduct.execute();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			deleteConfirmation = false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return deleteConfirmation;
	}// fin deleteProduct

	public List<Product> findByName(String name) {
		String sqlSelect = "SELECT ProductID, ProductName, Description, Price, UnistOnStock, pc.ProductCategoryID, ProductCategoryName"
				+ " FROM Product p JOIN ProductCategory pc ON p.ProductCategoryID = pc.ProductCategoryID"
				+ " WHERE ProductName LIKE CONCAT('" + name + "', '%')";
		return jdbcTemplate.query(sqlSelect, new ProductsExtractor());
	}// fin getProductsByTitle

	public List<Product> findByCategoryID(int idProductCategory) {
		Connection connection = null;
		List<Product> products = new LinkedList<Product>();

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelectFindByCategory = "SELECT ProductID, ProductName, Description, Price, UnistOnStock, p.ProductCategoryID FROM Product p INNER JOIN ProductCategory pc ON (p.ProductCategoryID = pc.ProductCategoryID) WHERE (p.ProductCategoryID = ?)";
			CallableStatement csSelectFindByCategory = connection.prepareCall(sqlSelectFindByCategory);
			csSelectFindByCategory.setInt(1, idProductCategory);
			ResultSet rs = csSelectFindByCategory.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt("ProductID"));
				product.setName(rs.getString("ProductName"));
				product.setDescription(rs.getString("Description"));
				product.setPrice(rs.getFloat("Price"));
				product.setUnitsOnStock(rs.getInt("UnistOnStock"));
				product.getCategory().setId(rs.getInt("ProductCategoryID"));
				;
				products.add(product);
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

		return products;
	}

	public List<Product> findByFamily(String nameFamily) {
		String sqlSelect = "SELECT p.ProductID, p.ProductName, p.Description, p.Price, p.UnistOnStock"
				+ " FROM Product p, Family_Product fp, Family f" + " WHERE f.FamilyName LIKE '%" + nameFamily
				+ "%' AND p.ProductID = fp.ProductID AND fp.FamilyID = f.FamilyID";
		return jdbcTemplate.query(sqlSelect, new ProductsExtractor());
	}

	public List<Product> getProductsByCategory(String name) {
		Connection connection = null;
		List<Product> products = new LinkedList<Product>();
		Map<Integer, Product> map = new HashMap<>();
		Product product = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String sqlSelect = "SELECT * FROM ProductCategory pc "
					+ "INNER JOIN Product p ON (p.ProductCategoryID = pc.ProductCategoryID) "
					+ "WHERE  ProductCategoryName = ?";
			CallableStatement csSelectGetAllCategory = connection.prepareCall(sqlSelect);
			csSelectGetAllCategory.setString(1, name);
			ResultSet rs = csSelectGetAllCategory.executeQuery();
			while (rs.next()) {
				Integer productID = rs.getInt("ProductID");
				product = map.get(productID);
				if (product == null) {
					// Se define la información básica del producto
					product = new Product();
					product.setId(productID);
					product.setName(rs.getString("ProductName"));
					product.setPrice(rs.getInt("Price"));
					product.setUnitsOnStock(rs.getInt("UnistOnStock"));
					product.setDescription(rs.getString("Description"));
					map.put(productID, product);
					products.add(product);
				}
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

		return products;
	}

}// fin ProductData Class

class ProductsExtractor implements ResultSetExtractor<List<Product>> {

	@Override
	public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Integer, Product> map = new HashMap<>();
		Product product = null;
		ProductCategory category = null;
		while (rs.next()) {
			Integer productID = rs.getInt("ProductID");
			product = map.get(productID);
			if (product == null) {
				// Se define la información básica del producto
				product = new Product();
				product.setId(productID);
				product.setName(rs.getString("ProductName"));
				product.setPrice(rs.getInt("Price"));
				product.setUnitsOnStock(rs.getInt("UnistOnStock"));
				product.setDescription(rs.getString("Description"));

				// Se defina la categoría del producto
				category = new ProductCategory();
				category.setName(rs.getString("ProductCategoryName"));
				category.setId(rs.getInt("ProductCategoryID"));
				product.setCategory(category);

				map.put(productID, product);
			}
		}
		return new ArrayList<Product>(map.values());
	}
}
