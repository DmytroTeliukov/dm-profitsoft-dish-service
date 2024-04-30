package com.dteliukov.profitsoftlab2.mappers;

import com.dteliukov.profitsoftlab2.dtos.CreateDishDto;
import com.dteliukov.profitsoftlab2.dtos.DishExcelObjDto;
import com.dteliukov.profitsoftlab2.dtos.GetDishDto;
import com.dteliukov.profitsoftlab2.dtos.GetFullDishInfoDto;
import com.dteliukov.profitsoftlab2.entities.Dish;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishMapper {
    GetDishDto ToGetDishDto(Dish dish);

    GetFullDishInfoDto ToGetFullDishInfoDto(Dish dish);

    Dish toDish(CreateDishDto dto);

    DishExcelObjDto ToDishExcelObjDto(Dish dish);
    List<DishExcelObjDto> ToGetDishExcelDtos(List<Dish> dishes);
}
