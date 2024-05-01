package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data transfer object (DTO) for filtering dishes response.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDishResponseDto {
    /**
     * The list of filtered dishes.
     */
    private List<GetDishDto> list;
    /**
     * The total number of pages after filtering.
     */
    private Integer totalPages;
}
