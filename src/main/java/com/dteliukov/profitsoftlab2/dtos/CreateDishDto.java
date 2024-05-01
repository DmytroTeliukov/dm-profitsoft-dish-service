package com.dteliukov.profitsoftlab2.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Data transfer object (DTO) for creating a dish.
 */
@Getter
@AllArgsConstructor
public class CreateDishDto {
    /**
     * The name of the dish.
     */
    @NotBlank(message = "Name of dish should not be blank")
    @Size(max = 64, message = "Length of dish name must be less than 64 characters")
    private String name;
    /**
     * The description of the dish.
     */
    @NotBlank(message = "Description of dish should not be blank")
    @Size(max = 512, message = "Length of dish description must be less than 512 characters")
    private String description;
    /**
     * The price of the dish.
     */
    @NotNull(message = "Price of dish should not be null")
    @Positive(message = "Price of dish should be positive value")
    private int price;
    /**
     * The weight of the dish.
     */
    @NotNull(message = "Weight of dish should not be null")
    @Positive(message = "Weight of dish should be positive value")
    private int weight;
    /**
     * The calories of the dish.
     */
    @NotNull(message = "Calories of dish should not be null")
    @Positive(message = "Calories of dish should be positive value")
    private float calories;
    /**
     * The ID of the category to which the dish belongs.
     */
    @NotNull(message = "CategoryId of dish should not be null")
    @Positive(message = "CategoryId of dish should be positive value")
    private Long categoryId;
    /**
     * The list of ingredients used in the dish.
     */
    @NotNull(message = "List of ingredients of dish should not be null")
    @Size(min = 1, max = 10, message = "List of ingredients should be greater than 1 and less than 10 items")
    private List<String> ingredients;
    /**
     * The list of cuisines associated with the dish.
     */
    @NotNull(message = "List of cuisines of dish should not be null")
    @Size(min = 1, max = 10, message = "List of cuisines should be greater than 1 and less than 10 items")
    private List<String> cuisines;
    /**
     * The list of dietary specifics related to the dish.
     */
    @NotNull(message = "List of dietary specifics of dish should not be null")
    @Size(min = 1, max = 10, message = "List of dietary specifics should be greater than 1 and less than 10 items")
    private List<String> dietarySpecifics;
}