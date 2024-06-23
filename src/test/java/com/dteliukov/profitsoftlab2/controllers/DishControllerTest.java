package com.dteliukov.profitsoftlab2.controllers;

import com.dteliukov.profitsoftlab2.ProfITsoftLab2SolutionApplication;
import com.dteliukov.profitsoftlab2.config.KafkaTestConfig;
import com.dteliukov.profitsoftlab2.dtos.*;
import com.dteliukov.profitsoftlab2.entities.Category;
import com.dteliukov.profitsoftlab2.entities.Dish;
import com.dteliukov.profitsoftlab2.parsers.impl.DishJacksonStreamingReaderParser;
import com.dteliukov.profitsoftlab2.repositories.CategoryRepository;
import com.dteliukov.profitsoftlab2.repositories.DishRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.dteliukov.profitsoftlab2.services.impl.DishServiceImpl.EXCEL_FILE_NAME_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProfITsoftLab2SolutionApplication.class)
@AutoConfigureMockMvc
public class DishControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private KafkaTemplate<String, EmailReceivedDto> kafkaTemplate;

    @MockBean
    private DishJacksonStreamingReaderParser readerParser;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() {
        dishRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("should return status 201 when create dish successfully")
    @Transactional
    public void shouldReturnStatus201_whenCreateDishSuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);

        // When
        ResultActions resultActions = mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated());

        Dish dish = dishRepository.findAll().get(0);

        assertThat(dish).isNotNull();
        assertThat(dish.getId()).isNotNull();
        assertThat(dish.getName()).isEqualTo(request.getName());
        assertThat(dish.getDescription()).isEqualTo(request.getDescription());
        assertThat(dish.getPrice()).isEqualTo(request.getPrice());
        assertThat(dish.getWeight()).isEqualTo(request.getWeight());
        assertThat(dish.getCalories()).isEqualTo(request.getCalories());
        assertThat(dish.getCategory().getId()).isEqualTo(categoryId);
        assertThat(dish.getIngredients().size()).isEqualTo(request.getIngredients().size());
        assertThat(dish.getIngredients().toArray()).isEqualTo(request.getIngredients().toArray());
        assertThat(dish.getDietarySpecifics().size()).isEqualTo(request.getDietarySpecifics().size());
        assertThat(dish.getDietarySpecifics().toArray()).isEqualTo(request.getDietarySpecifics().toArray());
        assertThat(dish.getCuisines().size()).isEqualTo(request.getCuisines().size());
        assertThat(dish.getCuisines().toArray()).isEqualTo(request.getCuisines().toArray());
    }

    @Test
    @DisplayName("should return status 400 when create dish with exist dish name")
    public void shouldReturnStatus400_whenCreateDishWithExistDishName() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);

        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // When
        ResultActions resultActions = mvc.perform(post("/api/dishes")
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
    @DisplayName("should return status 404 when create dish with not exist category")
    public void shouldReturnStatus404_whenCreateDishWithNotExistCategory() throws Exception {
        // Given
        CreateDishDto request = createSampleDishDto(1L);

        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // When
        ResultActions resultActions = mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 204 when update dish successfully")
    @Transactional
    public void shouldReturnStatus204_whenUpdateDishSuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);


        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        Dish dish = dishRepository.findAll().get(0);

        UpdateDishDto updateRequest = updateSampleDishDto();

        // When
        ResultActions resultActions = mvc.perform(put("/api/dishes/{id}", dish.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNoContent());

        dish = dishRepository.findAll().get(0);

        assertThat(dish).isNotNull();
        assertThat(dish.getId()).isNotNull();
        assertThat(dish.getName()).isNotEqualTo(request.getName());
        assertThat(dish.getName()).isEqualTo(updateRequest.getName());
        assertThat(dish.getDescription()).isNotEqualTo(request.getDescription());
        assertThat(dish.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(dish.getPrice()).isEqualTo(updateRequest.getPrice());
        assertThat(dish.getWeight()).isEqualTo(updateRequest.getWeight());
        assertThat(dish.getCalories()).isEqualTo(updateRequest.getCalories());
        assertThat(dish.getCategory().getId()).isEqualTo(categoryId);
        assertThat(dish.getIngredients().size()).isEqualTo(updateRequest.getIngredients().size());
        assertThat(dish.getIngredients().toArray()).isEqualTo(updateRequest.getIngredients().toArray());
        assertThat(dish.getDietarySpecifics().size()).isEqualTo(updateRequest.getDietarySpecifics().size());
        assertThat(dish.getDietarySpecifics().toArray()).isEqualTo(updateRequest.getDietarySpecifics().toArray());
        assertThat(dish.getCuisines().size()).isEqualTo(updateRequest.getCuisines().size());
        assertThat(dish.getCuisines().toArray()).isEqualTo(updateRequest.getCuisines().toArray());
    }

    @Test
    @DisplayName("should return status 400 when update dish with exist dish name")
    public void shouldReturnStatus400_whenUpdateDishWithExistDishName() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);


        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        Dish dish = dishRepository.findAll().get(0);

        UpdateDishDto updateRequest = updateSampleDishDto();
        updateRequest.setName(request.getName());

        // When
        ResultActions resultActions = mvc.perform(put("/api/dishes/{id}", dish.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return status 404 when update dish with not exist dish")
    public void shouldReturnStatus404_whenUpdateDishWithNotExistDish() throws Exception {
        // Given
        UpdateDishDto updateRequest = updateSampleDishDto();

        // When
        ResultActions resultActions = mvc.perform(put("/api/dishes/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 200 when get all dishes successfully")
    public void shouldReturnStatus200_whenAllDishesSuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();
        CreateDishDto request = createSampleDishDto(categoryId);

        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        FilterDishRequestDto filterDishRequestDto = new FilterDishRequestDto(categoryId,
                0,
                Integer.MAX_VALUE,
                0,
                1);

        // When
        ResultActions resultActions = mvc.perform(post("/api/dishes/_list")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDishRequestDto))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list.length()").value(1))
                .andExpect(jsonPath("$.list[0].name").value(request.getName()));
    }

    @Test
    @DisplayName("should return status 200 when get dish by id successfully")
    public void shouldReturnStatus200_whenGetDishByIdSuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);


        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        long dishId = dishRepository.findAll().get(0).getId();

        // When
        ResultActions resultActions = mvc.perform(get("/api/dishes/{id}", dishId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    @DisplayName("should return status 404 when get not exist dish by id")
    public void shouldReturnStatus404_whenGetNotExistDishById() throws Exception {
        // Given & When
        ResultActions resultActions = mvc.perform(get("/api/dishes/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 204 when delete dish by id successfully")
    public void shouldReturnStatus204_whenDeleteDishByIdSuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);

        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // When
        ResultActions resultActions = mvc.perform(get("/api/dishes/{id}", categoryId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 404 when delete not exist dish by id")
    public void shouldReturnStatus404_whenDeleteNotExistDishById() throws Exception {
        // Given & When
        ResultActions resultActions = mvc.perform(get("/api/dishes/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status 200 when generate report successfully")
    public void shouldReturnStatus200_whenGenerateReportSuccessfully() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        long categoryId = categoryRepository.save(sampleCategory).getId();

        CreateDishDto request = createSampleDishDto(categoryId);


        mvc.perform(post("/api/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        GenerateReportRequestDto requestDto = new GenerateReportRequestDto(categoryId,
                0,
                Integer.MAX_VALUE);

        String outputFilename = "\"" + String.format(EXCEL_FILE_NAME_PATTERN, categoryName, 0, Integer.MAX_VALUE) + "\"";

        // When & Then
        mvc.perform(post("/api/dishes/_report")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + outputFilename))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    @DisplayName("should return status 404 when there is not content for generating report")
    public void shouldReturnStatus404_whenThereIsNotContentForGeneratingReport() throws Exception {
        // Given
        String categoryName = "Dessert";
        Category sampleCategory = Category.builder().name(categoryName).build();
        categoryRepository.save(sampleCategory);


        GenerateReportRequestDto request = new GenerateReportRequestDto(1L,
                0,
                Integer.MAX_VALUE);

        // When & Then
        mvc.perform(post("/api/dishes/_report")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should return status 404 when category by id not exist")
    public void shouldReturnStatus404_whenCategoryByIdNotExist() throws Exception {
        // Given
        GenerateReportRequestDto request = new GenerateReportRequestDto(1L,
                0,
                Integer.MAX_VALUE);

        // When & Then
        mvc.perform(post("/api/dishes/_report")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should return status 201 when upload json file successfully")
    public void shouldReturnStatus201_whenUploadJsonFileSuccessfully() throws Exception {
        // Given
        Category category = categoryRepository.save(Category.builder().name("Category1").build());
        DishJsonObjDto dish1 = new DishJsonObjDto(
                "Grilled Salmon",
                "Freshly grilled salmon fillet with a side of vegetables.",
                15,
                250,
                350.5f,
                category.getId(),
                List.of("Salmon fillet", "Olive oil", "Salt", "Pepper", "Mixed vegetables"),
                List.of("Seafood", "Mediterranean"),
                List.of("High protein", "Low carb", "Gluten-free")
        );

        DishJsonObjDto dish2 = new DishJsonObjDto(
                "Chicken Caesar Salad",
                "Classic Caesar salad topped with grilled chicken strips.",
                12,
                300,
                280.5f,
                category.getId(),
                List.of("Romaine lettuce", "Grilled chicken", "Parmesan cheese", "Caesar dressing", "Croutons"),
                List.of("Italian", "American"),
                List.of("Low calorie", "High protein", "Gluten-free")
        );

        DishJsonObjDto dish3 = new DishJsonObjDto(
                "Vegetable Stir-Fry",
                "Assorted vegetables stir-fried with tofu in a savory sauce.",
                10,
                200,
                220.5f,
                category.getId(),
                List.of("Tofu", "Broccoli", "Bell peppers", "Carrots", "Soy sauce"),
                List.of("Asian", "Vegetarian"),
                List.of("Vegan", "Low carb", "Gluten-free")
        );

        when(readerParser.read(any()))
                .thenReturn(List.of(dish1, dish2, dish3));

        ClassPathResource resource = new ClassPathResource("dataset/all-valid-dishes.json");
        MockMultipartFile file = new MockMultipartFile("dish_json_file", "all-valid-dishes.json",
                MediaType.APPLICATION_JSON_VALUE, resource.getInputStream());

        // When & Then
        mvc.perform(MockMvcRequestBuilders.multipart("/api/dishes/upload")
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countSuccessfulUploadedRecords").value(3))
                .andExpect(jsonPath("$.countFailedUploadedRecords").value(0));

        List<Dish> dishes = dishRepository.findAll();
        assertThat(dishes.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("should return status 201 when upload json file with some failure records")
    public void shouldReturnStatus201_whenUploadJsonFileWithSomeFailureRecords() throws Exception {
        // Given
        Category category = categoryRepository.save(Category.builder().name("Category1").build());
        DishJsonObjDto dish1 = new DishJsonObjDto(
                "Grilled Salmon",
                "Freshly grilled salmon fillet with a side of vegetables.",
                15,
                250,
                350.5f,
                category.getId(),
                List.of("Salmon fillet", "Olive oil", "Salt", "Pepper", "Mixed vegetables"),
                List.of("Seafood", "Mediterranean"),
                List.of("High protein", "Low carb", "Gluten-free")
        );

        DishJsonObjDto dish2 = new DishJsonObjDto(
                "Chicken Caesar Salad",
                "",
                12,
                300,
                280.5f,
                category.getId(),
                List.of("Romaine lettuce", "Grilled chicken", "Parmesan cheese", "Caesar dressing", "Croutons"),
                List.of("Italian", "American"),
                List.of("Low calorie", "High protein", "Gluten-free")
        );

        DishJsonObjDto dish3 = new DishJsonObjDto(
                "Vegetable Stir-Fry",
                "Assorted vegetables stir-fried with tofu in a savory sauce.",
                10,
                200,
                220.5f,
                9L,
                List.of("Tofu", "Broccoli", "Bell peppers", "Carrots", "Soy sauce"),
                List.of("Asian", "Vegetarian"),
                List.of("Vegan", "Low carb", "Gluten-free")
        );

        when(readerParser.read(any()))
                .thenReturn(List.of(dish1, dish2, dish3));

        ClassPathResource resource = new ClassPathResource("dataset/partially-valid-dishes.json");
        MockMultipartFile file = new MockMultipartFile("dish_json_file", "partially-valid-dishes.json",
                MediaType.APPLICATION_JSON_VALUE, resource.getInputStream());

        // When & Then
        mvc.perform(MockMvcRequestBuilders.multipart("/api/dishes/upload")
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countSuccessfulUploadedRecords").value(1))
                .andExpect(jsonPath("$.countFailedUploadedRecords").value(2));

        List<Dish> dishes = dishRepository.findAll();
        assertThat(dishes.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("should return status 400 when upload json file with all failure records")
    public void shouldReturnStatus400_whenUploadJsonFileWithAllFailureRecords() throws Exception {
        // Given
        DishJsonObjDto dish1 = new DishJsonObjDto(
                "Grilled Salmon",
                "Freshly grilled salmon fillet with a side of vegetables.",
                15,
                250,
                350.5f,
                Long.MAX_VALUE,
                List.of("Salmon fillet", "Olive oil", "Salt", "Pepper", "Mixed vegetables"),
                List.of("Seafood", "Mediterranean"),
                List.of("High protein", "Low carb", "Gluten-free")
        );

        DishJsonObjDto dish2 = new DishJsonObjDto(
                "Chicken Caesar Salad",
                "",
                12,
                300,
                280.5f,
                Long.MAX_VALUE,
                List.of("Romaine lettuce", "Grilled chicken", "Parmesan cheese", "Caesar dressing", "Croutons"),
                List.of("Italian", "American"),
                List.of("Low calorie", "High protein", "Gluten-free")
        );

        DishJsonObjDto dish3 = new DishJsonObjDto(
                "Vegetable Stir-Fry",
                "Assorted vegetables stir-fried with tofu in a savory sauce.",
                10,
                200,
                220.5f,
                Long.MAX_VALUE,
                List.of("Tofu", "Broccoli", "Bell peppers", "Carrots", "Soy sauce"),
                List.of("Asian", "Vegetarian"),
                List.of("Vegan", "Low carb", "Gluten-free")
        );

        when(readerParser.read(any()))
                .thenReturn(List.of(dish1, dish2, dish3));

        ClassPathResource resource = new ClassPathResource("dataset/partially-valid-dishes.json");
        MockMultipartFile file = new MockMultipartFile("dish_json_file", "partially-valid-dishes.json",
                MediaType.APPLICATION_JSON_VALUE, resource.getInputStream());

        // When & Then
        mvc.perform(MockMvcRequestBuilders.multipart("/api/dishes/upload")
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countSuccessfulUploadedRecords").value(0))
                .andExpect(jsonPath("$.countFailedUploadedRecords").value(3));

        List<Dish> dishes = dishRepository.findAll();
        assertThat(dishes.size()).isEqualTo(0);
    }

    private static CreateDishDto createSampleDishDto(long categoryId) {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Chocolate");
        ingredients.add("Eggs");

        List<String> cuisines = new ArrayList<>();
        cuisines.add("International");

        List<String> dietarySpecifics = new ArrayList<>();
        dietarySpecifics.add("Gluten-Free");

        return new CreateDishDto(
                "Chocolate cake",
                "Tasty chocolate cake",
                100,
                200,
                300.5f,
                categoryId,
                ingredients,
                cuisines,
                dietarySpecifics
        );
    }

    private static UpdateDishDto updateSampleDishDto() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Chocolate");

        List<String> cuisines = new ArrayList<>();
        cuisines.add("International");

        List<String> dietarySpecifics = new ArrayList<>();
        dietarySpecifics.add("Vegetarian");

        return new UpdateDishDto(
                "Updated chocolate cake",
                "Updated chocolate cake",
                100,
                200,
                300.5f,
                ingredients,
                cuisines,
                dietarySpecifics
        );
    }
}
