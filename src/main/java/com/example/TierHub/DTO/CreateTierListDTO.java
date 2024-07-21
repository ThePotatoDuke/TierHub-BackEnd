package com.example.TierHub.DTO;

public record CreateTierListDTO(String description,
                                String name,
                                String imageUrl,
                                Long userId,
                                Long categoryId) {
}
