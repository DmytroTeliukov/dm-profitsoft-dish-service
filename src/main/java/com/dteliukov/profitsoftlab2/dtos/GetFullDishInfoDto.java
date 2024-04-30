package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data transfer object (DTO) for retrieving full information about a dish.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFullDishInfoDto {
    /*
    The ID of the dish.
    */
    private Long id;
    /**
     * The name of the dish.
     */
    private String name;
    /**
     * The description of the dish.
     */
    private String description;
    /**
     * The price of the dish.
     */
    private int price;
    /**
     * The weight of the dish.
     */
    private int weight;
    /**
     * The calories of the dish.
     */
    private float calories;
    /**
     * The category to which the dish belongs.
     */
    private CategoryDto category;
    /**
     * The list of ingredients used in the dish.
     */
    private List<String> ingredients;
    /**
     * The list of cuisines associated with the dish.
     */
    private List<String> cuisines;
    /**
     * The list of dietary specifics related to the dish.
     */
    private List<String> dietarySpecifics;
}
