package com.dteliukov.profitsoftlab2.repositories;

import com.dteliukov.profitsoftlab2.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Category entities.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /*
    Checks if a category with the given name exists.
    @param name The name of the category to check.
    @return True if a category with the given name exists, otherwise false.
    */
    boolean existsByName(String name);
}
