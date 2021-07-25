package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        when(userRepository.findByUsername("test")).thenReturn(getUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(getItem()));
    }

    @Test
    public void add_to_cart() throws Exception {
        final ResponseEntity<Cart> response = cartController.addTocart(getModifyCartRequest());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals("Beach ball", cart.getItems().get(0).getName());
    }

    @Test
    public void add_to_cart_username_not_found() throws Exception {
        ModifyCartRequest modifyCartRequest = getModifyCartRequest();
        modifyCartRequest.setUsername("testnotfound");

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart() throws Exception {
        final ResponseEntity<Cart> response = cartController.removeFromcart(getModifyCartRequest());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(0, response.getBody().getItems().size());
    }

    private ModifyCartRequest getModifyCartRequest() {
        ModifyCartRequest mcr = new ModifyCartRequest();
        mcr.setItemId(1L);
        mcr.setQuantity(1);
        mcr.setUsername("test");

        return mcr;
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(new Cart());

        return user;
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Beach ball");
        item.setDescription("Summer toy");
        item.setPrice(BigDecimal.valueOf(20.50));

        return item;
    }
}
