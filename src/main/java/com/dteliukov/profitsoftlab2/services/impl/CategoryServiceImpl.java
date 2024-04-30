package com.dteliukov.profitsoftlab2.services.impl;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.GetCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.UpdateCategoryDto;
import com.dteliukov.profitsoftlab2.entities.Category;
import com.dteliukov.profitsoftlab2.mappers.CategoryMapper;
import com.dteliukov.profitsoftlab2.repositories.CategoryRepository;
import com.dteliukov.profitsoftlab2.services.CategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GetCategoryDto> findAll() {
        return categoryMapper.toGetCategoryDtos(categoryRepository.findAll());
    }

    @Override
    public void save(CreateCategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);

        if (categoryRepository.existsByName(category.getName())) {
            throw new EntityExistsException(String.format("Category with name %s already exists", category.getName()));
        }

        categoryRepository.save(category);
    }

    @Override
    public void update(UpdateCategoryDto categoryDto, Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category by id {%d} not found", id)));

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new EntityExistsException(String.format("Category with name %s already exists", categoryDto.getName()));
        }

        foundCategory.setName(categoryDto.getName());
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Category by id {%d} not found", id));
        }
        categoryRepository.deleteById(id);
    }
}
