package com.dteliukov.profitsoftlab2.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenerateReportRequestDto {
    @NotNull(message = "CategoryId of dish should not be null")
    @Positive(message = "CategoryId of dish should be positive value")
    private Long categoryId;
    @NotNull(message = "Min price should not be null")
    @Positive(message = "Min price should be positive value")
    private Integer minPrice;
    @NotNull(message = "Max price should not be null")
    @Positive(message = "Max price should be positive value")
    private Integer maxPrice;
}
