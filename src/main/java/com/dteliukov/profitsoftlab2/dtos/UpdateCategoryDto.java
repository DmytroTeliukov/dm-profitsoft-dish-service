package com.dteliukov.profitsoftlab2.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Data transfer object (DTO) for updating a category.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    /**
     * The name of the category.
     */
    @NotBlank(message = "Name of category should not be blank")
    @Size(max = 32, message = "Length of category name must be less than 32 characters")
    private String name;
}
