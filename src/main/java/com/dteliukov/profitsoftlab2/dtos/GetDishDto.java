package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object (DTO) for retrieving a dish.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDishDto {
    /**
     * The ID of the dish.
     */
    private Long id;
    /**
     * The name of the dish.
     */
    private String name;
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
}
