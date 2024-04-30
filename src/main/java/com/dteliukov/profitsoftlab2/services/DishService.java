package com.dteliukov.profitsoftlab2.services;

import com.dteliukov.profitsoftlab2.dtos.*;
import org.springframework.web.multipart.MultipartFile;

public interface DishService {
    FilterDishResponseDto findAll(FilterDishRequestDto requestDto);
    GetFullDishInfoDto findById(Long id);
    void save(CreateDishDto dishDto);
    void update(UpdateDishDto dishDto, Long id);
    void deleteById(Long id);
    UploadResultDto uploadData(MultipartFile file);
    GenerateReportResponseDto generateReport(GenerateReportRequestDto requestDto);
}
