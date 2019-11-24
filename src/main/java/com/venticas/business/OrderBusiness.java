package com.venticas.business;

import com.venticas.data.OrderData;
import com.venticas.domain.Item;
import com.venticas.domain.Order;
import com.venticas.domain.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBusiness {

    @Autowired
    private OrderData orderData;

    public List<Order> getOrdersByUserId(int customerID) {
        return orderData.getOrdersByUserId(customerID);
    }

    public void saveOrder(String sessionID, ShoppingCart shoppingCart, Order order) {
        orderData.saveOrder(sessionID, shoppingCart, order);
    }

    public float getProductTax(Item item) {
        return orderData.getProductTax(item);
    }

}
