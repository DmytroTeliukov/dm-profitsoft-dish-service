package com.dteliukov.profitsoftlab2.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Data transfer object (DTO) for generating a report.
 */
@Getter
@AllArgsConstructor
public class GenerateReportRequestDto {
    /*
    The ID of the category to generate the report for.
    */
    @NotNull(message = "CategoryId of dish should not be null")
    @Positive(message = "CategoryId of dish should be positive value")
    private Long categoryId;
    /**
     * The minimum price to include in the report.
     */
    @NotNull(message = "Min price should not be null")
    @Positive(message = "Min price should be positive value")
    private Integer minPrice;
    /**
     * The maximum price to include in the report.
     */
    @NotNull(message = "Max price should not be null")
    @Positive(message = "Max price should be positive value")
    private Integer maxPrice;
}
