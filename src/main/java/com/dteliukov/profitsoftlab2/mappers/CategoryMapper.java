package com.dteliukov.profitsoftlab2.mappers;

import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.GetCategoryDto;
import com.dteliukov.profitsoftlab2.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * Mapper interface for mapping between Category entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    /**
     * Maps a Category entity to a GetCategoryDto.
     *
     * @param category The Category entity to map.
     * @return The corresponding GetCategoryDto.
     */
    GetCategoryDto toGetCategoryDto(Category category);

    /**
     * Maps a list of Category entities to a list of GetCategoryDto.
     *
     * @param categories The list of Category entities to map.
     * @return The corresponding list of GetCategoryDto.
     */
    List<GetCategoryDto> toGetCategoryDtos(List<Category> categories);

    /**
     * Maps a CreateCategoryDto to a Category entity.
     *
     * @param dto The CreateCategoryDto to map.
     * @return The corresponding Category entity.
     */
    Category toCategory(CreateCategoryDto dto);
}
