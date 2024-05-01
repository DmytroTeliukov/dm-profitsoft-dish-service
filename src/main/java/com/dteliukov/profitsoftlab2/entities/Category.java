package com.dteliukov.profitsoftlab2.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing a category in the database.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    /**
     * The unique identifier for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the category. It is unique and cannot be null.
     */
    @Column(nullable = false, unique = true, length = 32)
    private String name;
}