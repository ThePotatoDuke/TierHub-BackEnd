package com.example.TierHub.controllers;

import com.example.TierHub.DTO.CreateTierListDTO;
import com.example.TierHub.entities.*;
import com.example.TierHub.services.CategoryService;
import com.example.TierHub.services.TierListService;
import com.example.TierHub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tierhub/tier_lists")
public class TierListController {
    @Autowired
    private TierListService tierListService;

    @GetMapping
    public ResponseEntity<List<TierList>> getAllTierLists() {
        List<TierList> tierLists = tierListService.findAll();
        return ResponseEntity.ok(tierLists);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TierList> getTierListById(@PathVariable("id") Long id){
        Optional<TierList>  tierList = tierListService.findById(id);
        return tierList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<TierList> createTierList(@RequestBody CreateTierListDTO tierListDTO) {
        TierList createdTierList = tierListService.save(tierListDTO);
        return new ResponseEntity<>(createdTierList, HttpStatus.CREATED);
    }


    @PutMapping("/{tierListId}")
    public ResponseEntity<TierList> updateTierList(
            @PathVariable Long tierListId,
            @RequestBody CreateTierListDTO tierListDTO
    ) {
        TierList updatedTierList = tierListService.update(tierListId, tierListDTO);
        return new ResponseEntity<>(updatedTierList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTierList(@PathVariable Long id) {
        tierListService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<TierList>> searchTierLists(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String name) {

        List<TierList> tierLists = tierListService.search(categoryId,userId,name);

        if (tierLists == null || tierLists.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(tierLists);
        }
    }
    @GetMapping("/{tierListId}/items")
    public ResponseEntity<List<Item>> getItemsInTierList(@PathVariable Long tierListId) {
        List<Item> items = tierListService.findAllItemsInTierList(tierListId);

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(items);
        }
    }

    @GetMapping("/{tierListId}/tiers")
    public ResponseEntity<List<Tier>> getTiersInTierList(@PathVariable Long tierListId) {
        List<Tier> tiers = tierListService.findAllTiersInTierList(tierListId);
        if (tiers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(tiers);
        }
    }

}
