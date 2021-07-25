package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        when(userRepository.findByUsername("test")).thenReturn(getUser());
        when(orderRepository.findByUser(any())).thenReturn((getUserOrders()));
    }

    @Test
    public void submit() throws Exception {
        final ResponseEntity<UserOrder>  response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertEquals(BigDecimal.valueOf(61.30), userOrder.getTotal());
    }

    @Test
    public void submit_user_invalid() throws Exception {
        final ResponseEntity<UserOrder>  response = orderController.submit("test_invalid");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user() throws Exception {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();
        assertEquals(1, userOrders.size());
        assertEquals(2, userOrders.get(0).getItems().size());
    }

    @Test
    public void get_orders_for_user_invalid() throws Exception {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test_invalid");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(getItems());
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(61.30));

        user.setCart(cart);

        return user;
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Beach ball");
        item1.setDescription("Summer toy");
        item1.setPrice(BigDecimal.valueOf(20.50));

        items.add(item1);

        Item item2 = new Item();
        item1.setId(2L);
        item1.setName("Football ball");
        item1.setDescription("Sport toy");
        item1.setPrice(BigDecimal.valueOf(40.80));

        items.add(item2);

        return items;
    }

    private List<UserOrder> getUserOrders() {
        UserOrder userOrder = UserOrder.createFromCart(getUser().getCart());
        return Lists.list(userOrder);
    }
}
