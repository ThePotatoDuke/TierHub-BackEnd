package com.example.TierHub.services;

import com.example.TierHub.DTO.CreateTierDTO;
import com.example.TierHub.Exceptions.ResourceNotFoundException;
import com.example.TierHub.entities.Tier;
import com.example.TierHub.entities.TierList;
import com.example.TierHub.repos.TierListRepository;
import com.example.TierHub.repos.TierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TierService {
    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private TierListRepository tierListRepository;


    public List<Tier> findAll() {
        return tierRepository.findAll();
    }

    public Optional<Tier> findById(Long id) {
        return tierRepository.findById(id);
    }

    public Optional<TierList> findTierListById(Long id) {
        return tierListRepository.findById(id);
    }

    public Tier save(CreateTierDTO createTierDTO) {
        Tier tier = new Tier();
        tier.setName(createTierDTO.name());
        tier.setPosition(createTierDTO.position());
        tier.setColor(createTierDTO.color());

        TierList tierList = tierListRepository.findById(createTierDTO.tierListID())
                .orElseThrow(() -> new ResourceNotFoundException("Tier List not found with id " + createTierDTO.tierListID()));
        tier.setTierList(tierList);
        return tierRepository.save(tier);
    }

    public Tier update(Long id, CreateTierDTO createTierDTO) {
        Tier tier = tierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tier not found with id " + id));

        // Update fields if they are not null
        Optional.ofNullable(createTierDTO.name()).ifPresent(tier::setName);
        Optional.ofNullable(createTierDTO.position()).ifPresent(tier::setPosition);
        Optional.ofNullable(createTierDTO.color()).ifPresent(tier::setColor);

        // Update TierList if provided
        if (createTierDTO.tierListID() != null) {
            TierList tierList = tierListRepository.findById(createTierDTO.tierListID())
                    .orElseThrow(() -> new ResourceNotFoundException("TierList not found with id " + createTierDTO.tierListID()));
            tier.setTierList(tierList);
        }
        // Save and return the updated tier
        return tierRepository.save(tier);
    }

    public void deleteById(Long id) {
        tierRepository.deleteById(id);
    }

    public List<Tier> findTiersByTierListId(Long tierListId) {
        return tierRepository.findAllByTierListIdOrderByPosition(tierListId);
    }

}
