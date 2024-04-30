package com.dteliukov.profitsoftlab2.services;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.GetCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {
    List<GetCategoryDto> findAll();
    void save(CreateCategoryDto categoryDto);
    void update(UpdateCategoryDto categoryDto, Long id);
    void deleteById(Long id);
}
