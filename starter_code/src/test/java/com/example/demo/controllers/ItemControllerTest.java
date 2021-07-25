package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        when(itemRepository.findAll()).thenReturn(getItems());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(getItem()));
        when(itemRepository.findByName("Beach ball")).thenReturn(getItemsByName());
    }

    @Test
    public void get_items() throws Exception {
        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void get_item_by_id() throws Exception {
        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("Beach ball", item.getName());
    }

    @Test
    public void get_items_by_name() throws Exception {
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Beach ball");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Beach ball");
        item.setDescription("Summer toy");
        item.setPrice(BigDecimal.valueOf(20.50));

        return item;
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

    private List<Item> getItemsByName() {
        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Beach ball");
        item1.setDescription("Summer toy");
        item1.setPrice(BigDecimal.valueOf(20.50));

        items.add(item1);

        Item item2 = new Item();
        item1.setId(2L);
        item1.setName("Beach ball");
        item1.setDescription("Sport toy");
        item1.setPrice(BigDecimal.valueOf(40.80));

        items.add(item2);

        return items;
    }
}
