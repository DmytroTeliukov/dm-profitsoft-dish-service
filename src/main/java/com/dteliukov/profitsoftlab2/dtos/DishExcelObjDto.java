package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class DishExcelObjDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int weight;
    private float calories;
    private List<String> ingredients;
    private List<String> cuisines;
    private List<String> dietarySpecifics;
}
