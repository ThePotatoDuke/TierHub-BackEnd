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
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy="category", cascade = CascadeType.ALL)
    private Set<TierList> tierLists = new HashSet<>();
}
