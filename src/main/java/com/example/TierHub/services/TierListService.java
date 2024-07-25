package com.example.TierHub.services;

import com.example.TierHub.DTO.CreateTierListDTO;
import com.example.TierHub.DTO.GetTierListDTO;
import com.example.TierHub.DTO.TierListMapper;
import com.example.TierHub.Exceptions.ResourceNotFoundException;
import com.example.TierHub.Exceptions.StorageException;
import com.example.TierHub.entities.*;
import com.example.TierHub.repos.CategoryRepository;
import com.example.TierHub.repos.TierListRepository;
import com.example.TierHub.repos.TierRepository;
import com.example.TierHub.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TierListService {
    @Autowired
    private TierListRepository tierListRepository;
    @Autowired
    private TierRepository tierRepository;
    private static final Logger logger = LoggerFactory.getLogger(TierListService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<TierList> findAll() {
        return tierListRepository.findAll();
    }

    public Optional<TierList> findById(Long id) {
        return tierListRepository.findById(id);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
    private void mapDtoToEntity(TierList tierList, CreateTierListDTO tierListDTO) {
        if (tierListDTO.description() != null) {
            tierList.setDescription(tierListDTO.description());
        }
        if (tierListDTO.name() != null) {
            tierList.setName(tierListDTO.name());
        }
        if (tierListDTO.imageUrl() != null) {
            tierList.setImageUrl(tierListDTO.imageUrl());
        }
        if (tierListDTO.userId() != null) {
            User user = userRepository.findById(tierListDTO.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + tierListDTO.userId()));
            tierList.setUser(user);
        }

        if (tierListDTO.categoryId() != null) {
            Category category = categoryRepository.findById(tierListDTO.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + tierListDTO.categoryId()));
            tierList.setCategory(category);
        }
    }

    public TierList save(CreateTierListDTO tierListDTO) {
        // Create the default Tier
        Tier defaultTier = new Tier();
        defaultTier.setName("Default Tier");
        // Set other properties for defaultTier as needed

        // Save the default Tier
        Tier savedDefaultTier = tierRepository.save(defaultTier);

        // Create TierList
        TierList tierList = new TierList();
        tierList.setName(tierListDTO.name());
        tierList.setDescription(tierListDTO.description());
        tierList.setDefaultTier(savedDefaultTier);

        // Save the TierList
        return tierListRepository.save(tierList);
    }
    public TierList update(Long tierListId, CreateTierListDTO tierListDTO) {
        TierList tierList = tierListRepository.findById(tierListId)
                .orElseThrow(() -> new ResourceNotFoundException("TierList not found with id " + tierListId));
        mapDtoToEntity(tierList, tierListDTO);
        return tierListRepository.save(tierList);
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Attempting to delete TierList with id {}", id);
        if (!tierListRepository.existsById(id)) {
            logger.error("TierList with id {} not found", id);
            throw new EntityNotFoundException("TierList with id " + id + " not found");
        }
        tierListRepository.deleteById(id);
        logger.info("Successfully deleted TierList with id {}", id);

        // Verify the deletion
        boolean exists = tierListRepository.existsById(id);
        if (exists) {
            logger.error("TierList with id {} still exists after deletion attempt", id);
        } else {
            logger.info("TierList with id {} successfully deleted from database", id);
        }
    }

    public Optional<Category> findCategoryByID(Long id) {
        return categoryRepository.findById(id);
    }

    public List<TierList> findTierListsByCategory(Category category) {
        return tierListRepository.findByCategory(category);
    }

    public List<TierList> getTierListsByUser(User user) {
        return tierListRepository.findByUser(user);
    }

    public List<TierList> getTierListsByName(String name) {
        return tierListRepository.findByName(name);
    }

    public List<Tier> findAllTiersInTierList(Long tierListId) {
        TierList tierList = tierListRepository.findById(tierListId)
                .orElseThrow(() -> new RuntimeException("TierList not found"));

        return  new ArrayList<>(tierList.getTiers());
    }
    public List<Item> findAllItemsInTierList(Long tierListId) {
        TierList tierList = tierListRepository.findById(tierListId)
                .orElseThrow(() -> new RuntimeException("TierList not found"));

        Set<Tier> tiers = tierList.getTiers();

        return tiers.stream()
                .flatMap(tier -> tier.getItems().stream())
                .collect(Collectors.toList());
    }
    @Autowired
    private TierListMapper tierListMapper;

    public List<GetTierListDTO> getAllTierListsByUserId(Long userId) {
        List<TierList> tierLists = tierListRepository.findByUserId(userId);
        return tierLists.stream()
                .map(tierListMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<TierList> search(Long categoryId, Long userId, String name) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        Optional<User> user = userRepository.findById(userId);

        if (category.isPresent() && user.isPresent() && name != null) {
            return tierListRepository.findByCategoryAndUserAndName(category.get(), user.get(), name);
        } else if (category.isPresent() && user.isPresent()) {
            return tierListRepository.findByCategoryAndUser(category.get(), user.get());
        } else if (category.isPresent() && name != null) {
            return tierListRepository.findByCategoryAndName(category.get(), name);
        } else if (user.isPresent() && name != null) {
            return tierListRepository.findByUserAndName(user.get(), name);
        } else if (category.isPresent()) {
            return tierListRepository.findByCategory(category.get());
        } else if (user.isPresent()) {
            return tierListRepository.findByUser(user.get());
        } else if (name != null) {
            return tierListRepository.findByName(name);
        } else {
            // Handle case where none of category, user, or name is provCategoryed
            return null; // or return an empty list or throw an exception based on your business logic
        }
    }

    private final String UPLOAD_DIR = "src/main/resources/static/images";

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path destinationFile = Paths.get(UPLOAD_DIR).resolve(
                        Paths.get(fileName))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(Paths.get(UPLOAD_DIR).toAbsolutePath())) {
            throw new StorageException(
                    "Cannot store file outside current directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return "/images/" + fileName;
    }


}
