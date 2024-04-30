package com.dteliukov.profitsoftlab2.repositories;

import com.dteliukov.profitsoftlab2.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findByCategoryIdAndPriceBetween(Long categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);
    boolean existsByName(String name);
}
