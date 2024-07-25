package com.example.TierHub.DTO;

import com.example.TierHub.entities.Tier;

public record CreateTierListDTO(String description,
                                String name,
                                String imageUrl,
                                Long userId,
                                Long categoryId,
                                Tier defaultTier) {
}
