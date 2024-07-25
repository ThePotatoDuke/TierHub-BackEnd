package com.example.TierHub.DTO;

import com.example.TierHub.entities.Tier;
import lombok.Data;

import java.util.List;
@Data
public class TierListResponse {
    private List<Tier> tiers;
    private Tier defaultTier;

    // Getters and setters
}
