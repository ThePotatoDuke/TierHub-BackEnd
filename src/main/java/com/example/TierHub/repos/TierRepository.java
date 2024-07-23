package com.example.TierHub.repos;

import com.example.TierHub.entities.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TierRepository extends JpaRepository<Tier,Long> {
    List<Tier> findByTierListId(Long tierListId);
    @Query("SELECT t FROM Tier t WHERE t.tierList.id = :tierListId ORDER BY t.position")
    List<Tier> findAllByTierListIdOrderByPosition(Long tierListId);
}
