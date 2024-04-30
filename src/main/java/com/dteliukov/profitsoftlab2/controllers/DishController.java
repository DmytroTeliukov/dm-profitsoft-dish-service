package com.dteliukov.profitsoftlab2.controllers;

import com.dteliukov.profitsoftlab2.dtos.*;
import com.dteliukov.profitsoftlab2.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {
    private final DishService dishService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createDish(@RequestBody CreateDishDto dto) {
        dishService.save(dto);
    }

    @GetMapping("/{id}")
    public GetFullDishInfoDto getDishById(@PathVariable("id") Long id) {
        return dishService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDish(@PathVariable("id") Long id, @RequestBody UpdateDishDto dto) {
        dishService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable("id") Long id) {
        dishService.deleteById(id);
    }

    @PostMapping("/_list")
    public FilterDishResponseDto getAllDishes(@RequestBody FilterDishRequestDto filterDishRequestDto) {
        return dishService.findAll(filterDishRequestDto);
    }

    @PostMapping("/_report")
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

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public UploadResultDto upload(@RequestParam("dish_json_file") MultipartFile file) {
        return dishService.uploadData(file);
    }
}
