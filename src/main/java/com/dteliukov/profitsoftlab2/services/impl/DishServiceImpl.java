package com.dteliukov.profitsoftlab2.services.impl;

import com.dteliukov.profitsoftlab2.dtos.*;
import com.dteliukov.profitsoftlab2.entities.Category;
import com.dteliukov.profitsoftlab2.entities.Dish;
import com.dteliukov.profitsoftlab2.mappers.DishMapper;
import com.dteliukov.profitsoftlab2.parsers.ReaderParser;
import com.dteliukov.profitsoftlab2.parsers.WriterParser;
import com.dteliukov.profitsoftlab2.repositories.CategoryRepository;
import com.dteliukov.profitsoftlab2.repositories.DishRepository;
import com.dteliukov.profitsoftlab2.services.DishService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    public static final String EXCEL_FILE_NAME_PATTERN = "menu_by_category_%s_between_price_%d_and_%d.xlsx";

    private final Validator validator;
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final DishMapper dishMapper;
    private final ReaderParser<DishJsonObjDto> dishReaderParser;
    private final WriterParser<DishExcelObjDto> dishWriterParser;
    private final KafkaOperations<String, EmailReceivedDto> kafkaOperations;

    @Value("${kafka.topic.emailReceived}")
    private String emailReceivedTopic;

    @Override
    @Transactional(readOnly = true)
    public FilterDishResponseDto findAll(FilterDishRequestDto requestDto) {
        Page<Dish> dishes = dishRepository.findByCategoryIdAndPriceBetween(requestDto.getCategoryId(),
                        requestDto.getMinPrice(),
                        requestDto.getMaxPrice(),
                        PageRequest.of(requestDto.getPage(), requestDto.getSize()));

        System.out.println(dishes.getTotalElements());
        List<GetDishDto> dishDtos = dishes.getContent().stream().map(dishMapper::toGetDishDto).toList();


        return new FilterDishResponseDto(dishDtos, dishes.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public GetFullDishInfoDto findById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Dish with id %s not found", id)));

        return dishMapper.toGetFullDishInfoDto(dish);
    }

    @Override
    public void save(CreateDishDto dishDto) {
        Dish dish = dishMapper.toDish(dishDto);
        Category foundCategory = categoryRepository.findById(dishDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Category with id %s not found", dishDto.getCategoryId())));

        if (dishRepository.existsByName(dishDto.getName())) {
            throw new EntityExistsException(String.format("Dish with name %s already exists", dishDto.getName()));
        }

        dish.setCategory(foundCategory);

        dishRepository.save(dish);
        //sendDishModificationEmail(dish);
    }

    @Override
    public void update(UpdateDishDto dishDto, Long id) {
        Dish foundDish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Dish with id %s not found", id)));

        if (dishRepository.existsByName(dishDto.getName())) {
            throw new EntityExistsException(String.format("Dish with name %s already exists", dishDto.getName()));
        }

        foundDish.getIngredients().clear(); // Clear existing ingredients
        foundDish.getCuisines().clear();    // Clear existing cuisines
        foundDish.getDietarySpecifics().clear(); // Clear existing dietary specifics

        foundDish.setName(dishDto.getName());
        foundDish.setDescription(dishDto.getDescription());
        foundDish.setPrice(dishDto.getPrice());

        foundDish.setWeight(dishDto.getWeight());
        foundDish.setPrice(dishDto.getPrice());
        foundDish.setCalories(dishDto.getCalories());

        foundDish.getIngredients().addAll(dishDto.getIngredients());
        foundDish.getCuisines().addAll(dishDto.getCuisines());
        foundDish.getDietarySpecifics().addAll(dishDto.getDietarySpecifics());



        //sendDishModificationEmail(foundDish);
    }

    @Override
    public void deleteById(Long id) {
        dishRepository.deleteById(id);
    }

    @Override
    public GenerateReportResponseDto generateReport(GenerateReportRequestDto requestDto) {
        int minPrice = requestDto.getMinPrice();
        int maxPrice = requestDto.getMaxPrice();

        if (!categoryRepository.existsById(requestDto.getCategoryId())) {
            throw new EntityNotFoundException(String.format("Category with id %s not found", requestDto.getCategoryId()));
        }

        Page<Dish> dishes = dishRepository.findByCategoryIdAndPriceBetween(requestDto.getCategoryId(),
                requestDto.getMinPrice(),
                requestDto.getMaxPrice(),
                Pageable.unpaged());

        if (dishes.getTotalPages() == 0) {
            throw new EntityNotFoundException(
                    String.format("There is no dishes by categoryId {%d} in price range [%d-%d]",
                            requestDto.getCategoryId(),
                            minPrice,
                            maxPrice));
        }

        String categoryName = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow().getName();
        List<DishExcelObjDto> dishObjDtos = dishMapper.toGetDishExcelDtos(dishes.getContent());

        if (dishObjDtos.isEmpty()) {
            throw new EntityNotFoundException("There is no dishes by name " + categoryName);
        }

        String outputFilename = String.format(EXCEL_FILE_NAME_PATTERN, categoryName, minPrice, maxPrice);
        ByteArrayInputStream excelFileContent = dishWriterParser.write(dishObjDtos);

        return new GenerateReportResponseDto(outputFilename, excelFileContent);
    }

    @Override
    public UploadResultDto uploadData(MultipartFile file) {
        List<DishJsonObjDto> importDishData;

        try {
            importDishData = dishReaderParser.read(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int totalUploadedData = importDishData.size();

        Map<Long, Category> categories = categoryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        List<Dish> dishes = importDishData.stream()
                .map(dish -> convertToEntity(dish, categories))
                .filter(Objects::nonNull)
                .toList();

        dishRepository.saveAll(dishes);

        int countSuccessfulUploading = dishes.size();
        int countFailedUploading = totalUploadedData - countSuccessfulUploading;

        return new UploadResultDto(countSuccessfulUploading, countFailedUploading);
    }

    private Dish convertToEntity(DishJsonObjDto jsonObjDto, Map<Long, Category> categories) {
        Set<ConstraintViolation<DishJsonObjDto>> violations = validator.validate(jsonObjDto);
        Category category = categories.get(jsonObjDto.getCategoryId());

        if (!violations.isEmpty() || category == null) {
            return null;
        }

        return Dish.builder()
                .name(jsonObjDto.getName())
                .description(jsonObjDto.getDescription())
                .price(jsonObjDto.getPrice())
                .weight(jsonObjDto.getWeight())
                .calories(jsonObjDto.getCalories())
                .category(category)
                .cuisines(jsonObjDto.getCuisines())
                .ingredients(jsonObjDto.getIngredients())
                .dietarySpecifics(jsonObjDto.getDietarySpecifics())
                .build();
    }

    /*private void sendDishModificationEmail(Dish dish) {
        EmailReceivedDto email = EmailReceivedDto.builder()
                .recipient("test@example.com")
                .subject("Created new dish")
                .content(String.format("Hi, thank you for creating a new dish \"%s\".", dish.getName()))
                .build();


        kafkaOperations.send(emailReceivedTopic, email);
    }*/
}
