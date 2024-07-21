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
public class Tier {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int position;

    @Column(name = "color", length = 7)
    private String color;

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name= "tier_list_id", nullable = false)
    private TierList tierList;
}
