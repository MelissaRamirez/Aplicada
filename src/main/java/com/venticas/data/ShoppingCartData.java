package com.venticas.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.venticas.domain.Item;
import com.venticas.domain.ShoppingCart;

@Repository
public class ShoppingCartData {

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

	public ShoppingCart getShopingCartByCustomerID(int id) {
		Connection conexion = null;
		Map<Integer, ShoppingCart> cart = new HashMap<Integer, ShoppingCart>();
		Integer shoppingID = 0;
		try {
			conexion = dataSource.getConnection();
			conexion.setAutoCommit(false);

			PreparedStatement selectShoppingCart_item = conexion
					.prepareStatement("	SELECT * FROM ShoppingCart WHERE (CustomerID = ?)");
			selectShoppingCart_item.setInt(1, id);
			ResultSet rs = selectShoppingCart_item.executeQuery();
			while (rs.next()) {
				ShoppingCart shoppingCart = null;
				shoppingID = rs.getInt("ShoppingCartID");
				ShoppingCart shCart = cart.get(shoppingID);
				if (shCart == null) {
					shoppingCart = new ShoppingCart();
					shoppingCart.setId(shoppingID);
					shoppingCart.setDateCreated(rs.getString("DateCreated"));
					shoppingCart.getCustomer().setId(rs.getInt("CustomerID"));
					cart.put(shoppingID, shoppingCart);
				}
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

		return cart.get(shoppingID);
	}

	public List<Item> getItemsByShoppingCartID(int id) {
		Connection conexion = null;
		List<Item> items = new LinkedList<Item>();
		try {
			conexion = dataSource.getConnection();
			conexion.setAutoCommit(false);

			PreparedStatement selectShoppingCart_item = conexion
					.prepareStatement("SELECT * FROM Item WHERE (ShoppingCartID = ?)");
			selectShoppingCart_item.setInt(1, id);
			ResultSet rs = selectShoppingCart_item.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				item.getShoppingCart().setId(rs.getInt("ShoppingCartID"));
				item.getProduct().setId(rs.getInt("ProductID"));
				item.setQuantity(rs.getInt("Quantity"));
				items.add(item);
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

	public void insertShoppingCart(ShoppingCart cart) {
		Connection conexion = null;

		try {
			conexion = dataSource.getConnection();
			conexion.setAutoCommit(false);

			CallableStatement insertShoppingCartProc = conexion.prepareCall("{CALL insertShoppingCart(?, ?, ?)}");
			insertShoppingCartProc.setString(1, cart.getDateCreated());
			insertShoppingCartProc.setInt(2, cart.getCustomer().getId());
			insertShoppingCartProc.registerOutParameter(3, Types.INTEGER);
			insertShoppingCartProc.executeQuery();
			cart.setId(insertShoppingCartProc.getInt(3));

			conexion.commit();

		} catch (SQLException e) {
			try {
				conexion.rollback(); // por si el rollback no se ejecuta correractemnte
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
	}

	@Transactional(readOnly = true)
	public void insertItem(Item item) {
		Connection conexion = null;

		try {
			conexion = dataSource.getConnection();
			conexion.setAutoCommit(false);
			CallableStatement insertItem = conexion.prepareCall("{CALL insert_shoppingCart_item (?, ?, ?)}");
			insertItem.setInt(1, item.getProduct().getId());
			insertItem.setInt(2, item.getShoppingCart().getId());
			insertItem.setInt(3, item.getQuantity());
			insertItem.execute();
			conexion.commit();
		} catch (SQLException e) {
			try {
				conexion.rollback(); // por si el rollback no se ejecuta correractemnte
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
	}

	@Transactional(readOnly = true)
	public void updateItem(Item item) {
		Connection conexion = null;

		try {
			conexion = dataSource.getConnection();
			PreparedStatement updateShoppingCart_item = conexion
					.prepareStatement("UPDATE Item SET Quantity = ? WHERE (ShoppingCartID = ? AND ProductID = ?)");
			updateShoppingCart_item.setInt(1, item.getQuantity());
			updateShoppingCart_item.setInt(2, item.getShoppingCart().getId());
			updateShoppingCart_item.setInt(3, item.getProduct().getId());
			updateShoppingCart_item.executeUpdate();
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
	}

	@Transactional(readOnly = true)
	public void deleteItem(Item item) {
		Connection conexion = null;

		try {
			conexion = dataSource.getConnection();

			PreparedStatement deleteShoppingCart_item = conexion
					.prepareStatement("DELETE FROM Item WHERE (ShoppingCartID = ? AND ProductID = ?)");
			deleteShoppingCart_item.setInt(1, item.getShoppingCart().getId());
			deleteShoppingCart_item.setInt(2, item.getProduct().getId());
			deleteShoppingCart_item.executeUpdate();
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
	}

	@Transactional(readOnly = true)
	public List<Item> selectAllItems(ShoppingCart shoppingCart) {
		Connection conexion = null;
		List<Item> items = new LinkedList<Item>();

		try {
			conexion = dataSource.getConnection();

			PreparedStatement selectShoppingCart_item = conexion
					.prepareStatement("SELECT * FROM Item WHERE (ShoppingCartID = ?)");
			selectShoppingCart_item.setInt(1, shoppingCart.getId());
			ResultSet rs = selectShoppingCart_item.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				item.getShoppingCart().setId(rs.getInt("ShoppingCartID"));
				item.getProduct().setId(rs.getInt("ProductID"));
				item.setQuantity(rs.getInt("Quantity"));
				items.add(item);
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
	
	public Item getItem(int cartId, int productId) {
		Connection conexion = null;
		Item item = null;

		try {
			conexion = dataSource.getConnection();

			PreparedStatement selectShoppingCart_item = conexion
					.prepareStatement("SELECT * FROM Item WHERE (ShoppingCartID = ? && ProductID = ?)");
			selectShoppingCart_item.setInt(1, cartId);
			selectShoppingCart_item.setInt(2, productId);
			ResultSet rs = selectShoppingCart_item.executeQuery();
			while (rs.next()) {
				item = new Item();
				item.getShoppingCart().setId(rs.getInt("ShoppingCartID"));
				item.getProduct().setId(rs.getInt("ProductID"));
				item.setQuantity(rs.getInt("Quantity"));
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

		return item;	
	}

}
