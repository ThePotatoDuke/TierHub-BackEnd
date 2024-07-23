package com.example.TierHub.DTO;

import com.example.TierHub.entities.TierList;
import org.springframework.stereotype.Component;

@Component
public class TierListMapper {
    public GetTierListDTO toDTO(TierList tierList) {
        return new GetTierListDTO(
                tierList.getId(),
                tierList.getDescription(),
                tierList.getName(),
                tierList.getImageUrl()

        );
    }
}
