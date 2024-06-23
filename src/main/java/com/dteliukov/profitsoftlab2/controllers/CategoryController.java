package com.dteliukov.profitsoftlab2.controllers;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.EmailReceivedDto;
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
 * REST controller for managing categories.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated

//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return List of GetCategoryDto representing all categories.
     */
    @GetMapping
    public List<GetCategoryDto> getAllCategories() {
        return categoryService.findAll();
    }

    /**
     * Creates a new category.
     *
     * @param categoryDto The DTO containing category information.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody CreateCategoryDto categoryDto) {
        categoryService.save(categoryDto);
    }

    /**
     * Updates an existing category.
     *
     * @param categoryDto The DTO containing updated category information.
     * @param id          The ID of the category to update.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@RequestBody UpdateCategoryDto categoryDto, @PathVariable("id") Long id) {
        categoryService.update(categoryDto, id);
    }

    /**
     * Deletes a category by ID.
     *
     * @param id The ID of the category to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
    }

}