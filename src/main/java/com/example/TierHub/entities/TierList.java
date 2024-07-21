package com.example.TierHub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TierList {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "tierList", cascade = CascadeType.ALL)
    private Set<Tier> tiers = new HashSet<>();
}
