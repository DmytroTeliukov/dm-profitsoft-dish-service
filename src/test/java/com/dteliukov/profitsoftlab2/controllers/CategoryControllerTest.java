package com.dteliukov.profitsoftlab2.controllers;

import com.dteliukov.profitsoftlab2.ProfITsoftLab2SolutionApplication;
import com.dteliukov.profitsoftlab2.config.KafkaTestConfig;
import com.dteliukov.profitsoftlab2.dtos.CreateCategoryDto;
import com.dteliukov.profitsoftlab2.dtos.UpdateCategoryDto;
import com.dteliukov.profitsoftlab2.entities.Category;
import com.dteliukov.profitsoftlab2.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProfITsoftLab2SolutionApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("should return status 201 when create category successfully")
    public void shouldReturnStatus201_whenCreateCategorySuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        CreateCategoryDto request = new CreateCategoryDto(categoryName);

        // When
        ResultActions resultActions = mvc.perform(post("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated());

        Category category = categoryRepository.findAll().get(0);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo(categoryName);
    }

    @Test
    @DisplayName("should return status 400 when creating exist category")
    public void shouldReturnStatus400_whenCreatingExistCategory() throws Exception {
        // Given
        long categoryId = 1L;
        String categoryName = "Main Course";
        Category sampleCategory = new Category(categoryId, categoryName);
        CreateCategoryDto request = new CreateCategoryDto(categoryName);

        categoryRepository.save(sampleCategory);

        // When
        ResultActions resultActions = mvc.perform(post("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return status 200 when get all categories successfully")
    public void shouldReturnStatus200_whenGetAllCategoriesSuccessfully() throws Exception {
        // Given
        List<Category> data = List.of(
                Category.builder().name("Main Course").build(),
                Category.builder().name("Dessert").build());

        categoryRepository.saveAll(data);

        // When
        ResultActions resultActions = mvc.perform(get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Dessert"))
                .andExpect(jsonPath("$[1].name").value("Main Course"));

    }

    @Test
    @DisplayName("should return status 204 when delete category by id successfully")
    public void shouldReturnStatus204_whenDeleteCategoryByIdSuccessfully() throws Exception {
        // Given
        String oldCategoryName = "Main Course";
        Category sampleCategory = Category.builder().name(oldCategoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        // When
        ResultActions resultActions = mvc.perform(delete("/api/categories/{id}", categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return status 404 when failed delete category by id")
    public void shouldReturnStatus204_whenFailedDeleteCategoryById() throws Exception {
        // Given
        long categoryId = 1L;

        // When
        ResultActions resultActions = mvc.perform(delete("/api/categories/{id}", categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 404 when failed update category by id")
    public void shouldReturnStatus404_whenFailedUpdateCategoryById() throws Exception {
        // Given
        long categoryId = 1L;
        String newCategoryName = "Dessert";

        // When
        ResultActions resultActions = mvc.perform(put("/api/categories/{id}", categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateCategoryDto(newCategoryName)))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 400 when updating exist category name")
    public void shouldReturnStatus400_whenUpdatingExistCategoryName() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        // When
        ResultActions resultActions = mvc.perform(put("/api/categories/{id}", categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateCategoryDto(categoryName)))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return status 204 when update category by id successfully")
    public void shouldReturnStatus204_whenUpdateCategoryByIdSuccessfully() throws Exception {
        // Given
        String oldCategoryName = "Main Course";
        String newCategoryName = "Dessert";
        Category sampleCategory = Category.builder().name(oldCategoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();
        UpdateCategoryDto request = new UpdateCategoryDto(newCategoryName);

        // When
        ResultActions resultActions = mvc.perform(put("/api/categories/{id}", categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNoContent());

        Category category = categoryRepository.findAll().get(0);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(newCategoryName);
    }
}
