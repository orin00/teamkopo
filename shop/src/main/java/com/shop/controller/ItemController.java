package com.shop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shop.DTO.Item;
import com.shop.repository.ItemRepository;
import com.shop.service.ItemService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable("category") String category) {
        List<Item> items = itemRepository.findByCategory(category);

        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found for category: " + category);
        }

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemDetail(@PathVariable("id") Integer id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }

        return new ResponseEntity<>(itemOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        // 전달된 데이터 확인
        System.out.println("Received item: " + item);
        Item savedItem = itemService.addItem(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Integer id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
