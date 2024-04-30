package com.dteliukov.profitsoftlab2.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Data transfer object (DTO) for filtering dishes.
 */
@Getter
@AllArgsConstructor
public class FilterDishRequestDto {
    /*
    The ID of the category to filter by.
    */
    @NotNull(message = "CategoryId of dish should not be null")
    @Positive(message = "CategoryId of dish should be positive value")
    private Long categoryId;
    /**
     * The minimum price to filter by.
     */
    @NotNull(message = "Min price should not be null")
    @Positive(message = "Min price should be positive value")
    private Integer minPrice;
    /**
     * The maximum price to filter by.
     */
    @NotNull(message = "Max price should not be null")
    @Positive(message = "Max price should be positive value")
    private Integer maxPrice;
    /**
     * The page number for pagination.
     */
    @NotNull(message = "Page should not be null")
    @Min(value = 0, message = "Page should be greater or equals 0")
    private Integer page;
    /**
     * The size of each page for pagination.
     */
    @NotNull(message = "Size should not be null")
    @Positive(message = "Size should be positive value")
    private Integer size;
}
