package com.dteliukov.profitsoftlab2.services;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.GetCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.UpdateCategoryDto;

import java.util.List;

/**
 * Service interface for managing categories.
 */
public interface CategoryService {
    /**
     * Retrieves all categories.
     *
     * @return A list of GetCategoryDto representing all categories.
     */
    List<GetCategoryDto> findAll();

    /**
     * Saves a new category.
     *
     * @param categoryDto The data of the category to save.
     */
    void save(CreateCategoryDto categoryDto);

    /**
     * Updates an existing category.
     *
     * @param categoryDto The updated data of the category.
     * @param id          The ID of the category to update.
     */
    void update(UpdateCategoryDto categoryDto, Long id);

    /**
     * Deletes a category by ID.
     *
     * @param id The ID of the category to delete.
     */
    void deleteById(Long id);
}
