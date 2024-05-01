package com.dteliukov.profitsoftlab2.repositories;

import com.dteliukov.profitsoftlab2.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing Dish entities.
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    /**
     * Retrieves a page of dishes filtered by category ID and price range.
     *
     * @param categoryId The ID of the category to filter by.
     * @param minPrice   The minimum price.
     * @param maxPrice   The maximum price.
     * @param pageable   The pagination information.
     * @return A page of dishes filtered by category ID and price range.
     */
    Page<Dish> findByCategoryIdAndPriceBetween(Long categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);

    /**
     * Checks if a dish with the given name exists.
     *
     * @param name The name of the dish to check.
     * @return True if a dish with the given name exists, otherwise false.
     */
    boolean existsByName(String name);
}
