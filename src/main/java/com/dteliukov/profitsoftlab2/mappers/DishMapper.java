package com.dteliukov.profitsoftlab2.mappers;

import com.dteliukov.profitsoftlab2.dtos.CreateDishDto;
import com.dteliukov.profitsoftlab2.dtos.DishExcelObjDto;
import com.dteliukov.profitsoftlab2.dtos.GetDishDto;
import com.dteliukov.profitsoftlab2.dtos.GetFullDishInfoDto;
import com.dteliukov.profitsoftlab2.entities.Dish;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for mapping between Dish entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface DishMapper {
    /*
    Maps a Dish entity to a GetDishDto.
    @param dish The Dish entity to map.
    @return The corresponding GetDishDto.
    */
    GetDishDto ToGetDishDto(Dish dish);

    /**
     * Maps a Dish entity to a GetFullDishInfoDto.
     *
     * @param dish The Dish entity to map.
     * @return The corresponding GetFullDishInfoDto.
     */
    GetFullDishInfoDto ToGetFullDishInfoDto(Dish dish);

    /**
     * Maps a CreateDishDto to a Dish entity.
     *
     * @param dto The CreateDishDto to map.
     * @return The corresponding Dish entity.
     */
    Dish toDish(CreateDishDto dto);

    /**
     * Maps a Dish entity to a DishExcelObjDto.
     *
     * @param dish The Dish entity to map.
     * @return The corresponding DishExcelObjDto.
     */
    DishExcelObjDto ToDishExcelObjDto(Dish dish);

    /**
     * Maps a list of Dish entities to a list of DishExcelObjDto.
     *
     * @param dishes The list of Dish entities to map.
     * @return The corresponding list of DishExcelObjDto.
     */
    List<DishExcelObjDto> ToGetDishExcelDtos(List<Dish> dishes);
}