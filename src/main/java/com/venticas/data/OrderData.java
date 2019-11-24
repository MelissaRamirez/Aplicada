package com.venticas.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.venticas.domain.Item;
import com.venticas.domain.Order;
import com.venticas.domain.ShoppingCart;

@Repository
public class OrderData {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Order> getOrdersByUserId(int customerId){
        String sqlSelect = "SELECT OrderID, OrderDate, ShipingAddress, ShipDate, TrakingNumber, TotalValue" +
                " FROM Orders" +
                " WHERE CustomerID = " + customerId;
        return jdbcTemplate.query(sqlSelect, new OrdersExtractor());
    }

    public void updateOrderStatus(int orderStatusId, int orderId) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String sqlUpdate = "UPDATE Orders" +
                    " SET OrderStatusID = ?" +
                    " WHERE OrderID = ?";
            PreparedStatement sqlPsUpdate = connection.prepareCall(sqlUpdate);
            sqlPsUpdate.setInt(1, orderStatusId);
            sqlPsUpdate.setInt(2, orderId);
            sqlPsUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { // se finaliza la conexion
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    
    public boolean saveOrder(String sessionID, ShoppingCart shoppingCart, Order order) {

        Connection connection = null;
        boolean state = true;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            String sqlProcedure;
            CallableStatement csSqlProcedure;

            //Se hacer el insert en Order
            sqlProcedure = "{call save_order(?,?,?,?,?,?,?)}";
            csSqlProcedure = connection.prepareCall(sqlProcedure);
            csSqlProcedure.setInt("status_id", 1);  //siempre pendiente
            csSqlProcedure.setString("order_date", LocalDateTime.now().toString());
            csSqlProcedure.setString("shiping_addr", order.getShippingAddress());
            csSqlProcedure.setString("tracking_num", order.getTrackingNumber());
            csSqlProcedure.setFloat("total_val", order.getTotalValue());
            csSqlProcedure.setInt("customer_id", Integer.parseInt(sessionID));
            csSqlProcedure.execute();
            int orderID = csSqlProcedure.getInt("order_id");

            for (Item item: shoppingCart.getItems()) {
                sqlProcedure = "{call save_order_detail(?,?,?,?)}";
                csSqlProcedure = connection.prepareCall(sqlProcedure);
                csSqlProcedure.setInt("order_id", orderID);
                csSqlProcedure.setInt("product_id", item.getProduct().getId());
                csSqlProcedure.setInt("quantity", item.getQuantity());
                csSqlProcedure.setFloat("price", item.getProduct().getPrice());
                csSqlProcedure.execute();
            }
            connection.commit();

        } catch (SQLException e) {
            state = false;
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
        return state;
    }

    public float getProductTax(Item item) {

        Connection connection = null;
        float taxPercentage = 0;
        try {
            connection = dataSource.getConnection();
            String sqlProcedure= "{call product_tax(?,?)}";
            CallableStatement csSqlProcedure = connection.prepareCall(sqlProcedure);
            csSqlProcedure.setInt("product_id", item.getProduct().getId());
            csSqlProcedure.execute();
            taxPercentage = csSqlProcedure.getInt("tax_percentage");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally { // se finaliza la conexion
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return taxPercentage;
    }// fin saveOrder

}// fin OrderData

class OrdersExtractor implements ResultSetExtractor<List<Order>> {

    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Order> map = new HashMap<>();
        Order order = null;
        while (rs.next()) {
            Integer orderID = rs.getInt("OrderID");
            order = map.get(orderID);
            if(order == null) {
                order = new Order();
                order.setId(orderID);
                order.setTotalValue(rs.getInt("TotalValue"));
                order.setTrackingNumber(rs.getString("TrakingNumber"));
                order.setShippingAddress(rs.getString("ShipingAddress"));
                order.setOrderDate(rs.getString("OrderDate"));
                order.setShipDate(rs.getString("ShipDate"));
                map.put(orderID, order);
            }
        }
        return new ArrayList<>(map.values());
    }
}
