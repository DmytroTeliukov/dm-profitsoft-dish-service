package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFullDishInfoDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int weight;
    private float calories;
    private CategoryDto category;
    private List<String> ingredients;
    private List<String> cuisines;
    private List<String> dietarySpecifics;
}
