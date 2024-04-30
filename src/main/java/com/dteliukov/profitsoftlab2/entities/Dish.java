package com.dteliukov.profitsoftlab2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


/**
 * Entity class representing a dish.
 */
@Entity
@Table(name = "dishes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    /*
    The unique identifier of the dish.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the dish.
     */
    @Column(nullable = false, unique = true, length = 64)
    private String name;
    /**
     * The description of the dish.
     */
    @Column(nullable = false, length = 512)
    private String description;
    /**
     * The price of the dish.
     */
    @Column(nullable = false)
    private int price;
    /**
     * The weight of the dish.
     */
    @Column(nullable = false)
    private int weight;
    /**
     * The calories of the dish.
     */
    @Column(nullable = false)
    private float calories;
    /**
     * The category to which the dish belongs.
     */
    @ManyToOne
    @JoinColumn(name = "fk_category_id", referencedColumnName = "id")
    private Category category;
    /**
     * The list of ingredients used in the dish.
     */
    @ElementCollection
    @CollectionTable(name = "dish_ingredients", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "name")
    private List<String> ingredients;
    /**
     * The list of cuisines associated with the dish.
     */
    @ElementCollection
    @CollectionTable(name = "dish_cuisines", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "name")
    private List<String> cuisines;
    /**
     * The list of dietary specifics related to the dish.
     */
    @ElementCollection
    @CollectionTable(name = "dish_dietary_specifics", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "name")
    private List<String> dietarySpecifics;
}