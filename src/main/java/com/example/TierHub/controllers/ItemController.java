package com.example.TierHub.controllers;

import com.example.TierHub.DTO.CreateItemDTO;
import com.example.TierHub.Exceptions.ResourceNotFoundException;
import com.example.TierHub.entities.Item;
import com.example.TierHub.entities.Item;
import com.example.TierHub.entities.Tier;
import com.example.TierHub.entities.User;
import com.example.TierHub.services.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> itemLists = itemService.findAll();
        return ResponseEntity.ok(itemLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id) {
        Optional<Item> itemList = itemService.findById(id);
        return itemList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody CreateItemDTO itemDTO) {
        try{
            Item createdItem = itemService.save(itemDTO);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody CreateItemDTO createItemDTO) {
        try {
            Item updatedItem = itemService.update(id, createItemDTO);
            return ResponseEntity.ok(updatedItem);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tier/{tierId}")
    public ResponseEntity<List<Item>> getItemsByTierId(@PathVariable Long tierId) {
        List<Item> items = itemService.findItemsByTierId(tierId);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }

    
}




