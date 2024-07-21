package com.example.TierHub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;

    @Column(name = "username")
    private String userName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<TierList> tierLists = new HashSet<>();
}
