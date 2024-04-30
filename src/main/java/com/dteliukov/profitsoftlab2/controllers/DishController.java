package com.dteliukov.profitsoftlab2.controllers;

import com.dteliukov.profitsoftlab2.dtos.*;
import com.dteliukov.profitsoftlab2.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * Controller for managing dishes.
 */
@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {
    private final DishService dishService;

    /**
     * Creates a new dish.
     *
     * @param dto The CreateDishDto containing dish data.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createDish(@RequestBody CreateDishDto dto) {
        dishService.save(dto);
    }

    /**
     * Retrieves a dish by ID.
     *
     * @param id The ID of the dish to retrieve.
     * @return GetFullDishInfoDto containing the dish information.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetFullDishInfoDto getDishById(@PathVariable("id") Long id) {
        return dishService.findById(id);
    }

    /**
     * Updates an existing dish.
     *
     * @param id  The ID of the dish to update.
     * @param dto The UpdateDishDto containing updated dish data.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDish(@PathVariable("id") Long id, @RequestBody UpdateDishDto dto) {
        dishService.update(dto, id);
    }

    /**
     * Deletes a dish by ID.
     *
     * @param id The ID of the dish to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable("id") Long id) {
        dishService.deleteById(id);
    }

    /**
     * Retrieves all dishes based on filtering criteria.
     *
     * @param filterDishRequestDto The FilterDishRequestDto containing filtering criteria.
     * @return FilterDishResponseDto containing filtered dishes.
     */
    @PostMapping(value = "/_list",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FilterDishResponseDto getAllDishes(@RequestBody FilterDishRequestDto filterDishRequestDto) {
        return dishService.findAll(filterDishRequestDto);
    }

    /**
     * Generates a report based on provided criteria.
     *
     * @param generateReportRequestDto The GenerateReportRequestDto containing report generation criteria.
     * @return ResponseEntity containing the generated report as a file.
     */
    @PostMapping(value = "/_report",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> generateReport(@RequestBody GenerateReportRequestDto generateReportRequestDto) {
        GenerateReportResponseDto generateReportResponseDto = dishService.generateReport(generateReportRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(generateReportResponseDto.getName()).build());
        InputStreamResource file = new InputStreamResource(generateReportResponseDto.getContent());
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    /**
     * Uploads dish data from a JSON file.
     *
     * @param file The JSON file containing dish data.
     * @return UploadResultDto containing upload result information.
     */
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadResultDto upload(@RequestParam("dish_json_file") MultipartFile file) {
        return dishService.uploadData(file);
    }
}
