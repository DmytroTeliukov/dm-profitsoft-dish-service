package com.dteliukov.profitsoftlab2.mappers;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.GetCategoryDto;
import com.dteliukov.profitsoftlab2.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    GetCategoryDto toGetCategoryDto(Category category);
    List<GetCategoryDto> toGetCategoryDtos(List<Category> categories);
    Category toCategory(CreateCategoryDto dto);
}
