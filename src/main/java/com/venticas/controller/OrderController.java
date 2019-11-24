package com.venticas.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.venticas.business.CustomerBusiness;
import com.venticas.business.OrderBusiness;
import com.venticas.business.ProductBusiness;
import com.venticas.business.ShoppingCartBusiness;
import com.venticas.domain.Address;
import com.venticas.domain.Customer;
import com.venticas.domain.Item;
import com.venticas.domain.Order;
import com.venticas.domain.ShoppingCart;
import com.venticas.domain.User;

@Controller
public class OrderController {

    @Autowired
    private CustomerBusiness customerBusiness;

    @Autowired
    private ShoppingCartBusiness shoppingCartBusiness;

    @Autowired
    private OrderBusiness orderBusiness;

    @Autowired
    private ProductBusiness productBusiness;

    @RequestMapping(value = "/ordersDetails", method = RequestMethod.GET)
    public String getOrdersDetails(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("orders", orderBusiness.getOrdersByUserId(user.getId()));
        return "orders/orderDetailsView";
    }

    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    public String saveOrder(Order order, Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        Customer customer = customerBusiness.findCustomerByID(user);

        ShoppingCart shoppingCart = shoppingCartBusiness.getShopingCartByCustomerID(customer.getId());
        shoppingCart.setItems(shoppingCartBusiness.getItemsByShoppingCartID(shoppingCart.getId()));
        for (Item item: shoppingCart.getItems()) {
            item.setProduct(productBusiness.findProductById(item.getProduct().getId()));
            shoppingCartBusiness.deleteItem(item);
        }

        model.addAttribute("order", order);
        orderBusiness.saveOrder(Integer.toString(customer.getId()), shoppingCart, order);
        return "home";
    }

    @RequestMapping(value = "/saveOrder", method = RequestMethod.GET)
    public String saveOrder(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Customer customer = customerBusiness.findCustomerByID(user);

        float totalValue = 0;
        ShoppingCart shoppingCart = shoppingCartBusiness.getShopingCartByCustomerID(customer.getId());
        shoppingCart.setItems(shoppingCartBusiness.getItemsByShoppingCartID(shoppingCart.getId()));
        for (Item item: shoppingCart.getItems()) {
            item.setProduct(productBusiness.findProductById(item.getProduct().getId()));
            totalValue = ((orderBusiness.getProductTax(item)/100) * item.getProduct().getPrice()) + item.getProduct().getPrice();
            totalValue += totalValue * item.getQuantity();
        }

        Order order = new Order();
        order.setTrackingNumber(Integer.toString((int )(Math.random() * 5000 + 1)));
        order.setTotalValue(totalValue);

        List<Address> addresses = customer.getAddress();
        int customerID = customer.getId();

        model.addAttribute("addresses", addresses);
        model.addAttribute("customerID", customerID);
        model.addAttribute("order", order);
        model.addAttribute("shoppingCart", shoppingCart);

        return "orders/saveOrderView";
    }
}
