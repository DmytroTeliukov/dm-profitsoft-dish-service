package com.dteliukov.profitsoftlab2.controllers;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.GetCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.UpdateCategoryDto;
import com.dteliukov.profitsoftlab2.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing categories.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return List of GetCategoryDto containing all categories.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetCategoryDto> getAllCategories() {
        return categoryService.findAll();
    }

    /**
     * Creates a new category.
     *
     * @param categoryDto The CreateCategoryDto containing category data.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody CreateCategoryDto categoryDto) {
        categoryService.save(categoryDto);
    }

    /**
     * Updates an existing category.
     *
     * @param categoryDto The UpdateCategoryDto containing updated category data.
     * @param id          The ID of the category to update.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@RequestBody UpdateCategoryDto categoryDto, @PathVariable("id") Long id) {
        categoryService.update(categoryDto, id);
    }

    /**
     * Deletes a category by ID.
     *
     * @param id The ID of the category to delete.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
    }
}