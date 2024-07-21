package com.example.TierHub.repos;

import com.example.TierHub.entities.Category;
import com.example.TierHub.entities.TierList;
import com.example.TierHub.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TierListRepository extends JpaRepository<TierList, Long> {
    List<TierList> findByCategory(Category category);

    List<TierList> findByUser(User user);

    List<TierList> findByName(String name);

    List<TierList> findByCategoryAndUser(Category category, User user);

    List<TierList> findByCategoryAndName(Category category, String name);

    List<TierList> findByUserAndName(User user, String name);

    List<TierList> findByCategoryAndUserAndName(Category category, User user, String name);
}
