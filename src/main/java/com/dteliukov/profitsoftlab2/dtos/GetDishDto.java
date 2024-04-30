package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDishDto {
    private Long id;
    private String name;
    private int price;
    private int weight;
    private float calories;
    private CategoryDto category;
}
