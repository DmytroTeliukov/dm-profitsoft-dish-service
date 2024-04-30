package com.dteliukov.profitsoftlab2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String name;
    @Column(nullable = false, length = 512)
    private String description;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int weight;
    @Column(nullable = false)
    private float calories;

    @ManyToOne
    @JoinColumn(name = "fk_category_id", referencedColumnName = "id")
    private Category category;

    @ElementCollection
    @CollectionTable(name = "dish_ingredients", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "name")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "dish_cuisines", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "name")
    private List<String> cuisines;

    @ElementCollection
    @CollectionTable(name = "dish_dietary_specifics", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "name")
    private List<String> dietarySpecifics;
}
