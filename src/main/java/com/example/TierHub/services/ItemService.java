package com.example.TierHub.services;

import com.example.TierHub.DTO.CreateItemDTO;
import com.example.TierHub.DTO.CreateTierDTO;
import com.example.TierHub.Exceptions.ResourceNotFoundException;
import com.example.TierHub.entities.Item;
import com.example.TierHub.entities.Tier;
import com.example.TierHub.entities.TierList;
import com.example.TierHub.repos.ItemRepository;
import com.example.TierHub.repos.TierRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TierRepository tierRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(CreateItemDTO itemDTO) {
        Tier tier = tierRepository.findById(itemDTO.tierID()).orElseThrow(ResourceNotFoundException::new);

        Item newItem = new Item();
        newItem.setImageUrl(itemDTO.imageURL());
        newItem.setOrderNo(itemDTO.orderNo());
        newItem.setTier(tier);

        return itemRepository.save(newItem);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }


    public Item update(Long id, CreateItemDTO createItemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tier not found with id " + id));

        // Update fields if they are not null
        Optional.ofNullable(createItemDTO.imageURL()).ifPresent(item::setImageUrl);
        Optional.of(createItemDTO.orderNo()).ifPresent(item::setOrderNo);

        if (createItemDTO.tierID() != null) {
            Tier tier = tierRepository.findById(createItemDTO.tierID())
                    .orElseThrow(ResourceNotFoundException::new);
            item.setTier(tier);
        }
        return itemRepository.save(item);
    }

    public Optional<Tier> findTierById(Long id) {
        return tierRepository.findById(id);
    }
    public List<Item> findItemsByTierId(Long tierId) {
        return itemRepository.findByTierId(tierId);
    }
}
