package com.dteliukov.profitsoftlab2.services;

import com.dteliukov.profitsoftlab2.dtos.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service interface for managing dishes.
 */
public interface DishService {
    /*
    Retrieves dishes based on filtering criteria.
    @param requestDto The filtering criteria.
    @return A FilterDishResponseDto containing filtered dishes.
    */
    FilterDishResponseDto findAll(FilterDishRequestDto requestDto);

    /**
     * Retrieves full information about a dish by ID.
     *
     * @param id The ID of the dish to retrieve.
     * @return A GetFullDishInfoDto containing information about the dish.
     */
    GetFullDishInfoDto findById(Long id);

    /**
     * Saves a new dish.
     *
     * @param dishDto The data of the dish to save.
     */
    void save(CreateDishDto dishDto);

    /**
     * Updates an existing dish.
     *
     * @param dishDto The updated data of the dish.
     * @param id      The ID of the dish to update.
     */
    void update(UpdateDishDto dishDto, Long id);

    /**
     * Deletes a dish by ID.
     *
     * @param id The ID of the dish to delete.
     */
    void deleteById(Long id);

    /**
     * Uploads data from a file.
     *
     * @param file The file containing the data to upload.
     * @return An UploadResultDto containing the upload result.
     */
    UploadResultDto uploadData(MultipartFile file);

    /**
     * Generates a report based on the given criteria.
     *
     * @param requestDto The criteria for generating the report.
     * @return A GenerateReportResponseDto containing the generated report.
     */
    GenerateReportResponseDto generateReport(GenerateReportRequestDto requestDto);
}
