package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Data transfer object (DTO) for retrieving a category.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryDto {
    /*
    The ID of the category.
    */
    private Long id;
    /**
     * The name of the category.
     */
    private String name;
}
