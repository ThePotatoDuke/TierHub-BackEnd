package com.example.TierHub.DTO;

import com.example.TierHub.entities.Item;

import java.util.Set;

public record CreateTierDTO(
        String name,
        int position,
        String color,
        Long tierListID,
        Set<Long> itemIds // Use item IDs instead of entire item objects
) {}