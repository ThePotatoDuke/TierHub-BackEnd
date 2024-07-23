package com.example.TierHub.controllers;

import com.example.TierHub.DTO.CreateTierDTO;
import com.example.TierHub.Exceptions.ResourceNotFoundException;
import com.example.TierHub.entities.Item;
import com.example.TierHub.entities.Tier;
import com.example.TierHub.entities.TierList;
import com.example.TierHub.services.TierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tiers")
public class TierController {
    @Autowired
    TierService tierService;

    @GetMapping
    public ResponseEntity<List<Tier>> getAllTiers() {
        List<Tier> tierLists = tierService.findAll();
        return ResponseEntity.ok(tierLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tier> getTierById(@PathVariable("id") Long id){
        Optional<Tier> tierList = tierService.findById(id);
        return tierList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Tier> createTier(@RequestBody CreateTierDTO createTierDTO) {
        try{
            Tier createdTier = tierService.save(createTierDTO);
            return new ResponseEntity<>(createdTier, HttpStatus.CREATED);
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Tier> updateTier(@PathVariable Long id, @RequestBody CreateTierDTO createTierDTO) {
        try {
            Tier updatedTier = tierService.update(id, createTierDTO);
            return ResponseEntity.ok(updatedTier);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTier(@PathVariable Long id) {
        tierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tier_list/{tierListId}")
    public ResponseEntity<List<Tier>> getItemsByTierId(@PathVariable Long tierListId) {
        List<Tier> tiers = tierService.findTiersByTierListId(tierListId);
        if (tiers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tiers);
    }
    
}
