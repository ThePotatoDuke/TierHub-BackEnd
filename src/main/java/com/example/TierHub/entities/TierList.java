package com.example.TierHub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TierList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "tierList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Tier> tiers = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "default_tier_id")
    private Tier defaultTier;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TierList)) return false;
        TierList tierList = (TierList) o;
        return Objects.equals(id, tierList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
