package com.dteliukov.profitsoftlab2.parsers.impl;

import com.dteliukov.profitsoftlab2.dtos.DishExcelObjDto;
import com.dteliukov.profitsoftlab2.parsers.WriterParser;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DishExcelWriterParser implements WriterParser<DishExcelObjDto> {

    @Override
    public ByteArrayInputStream write(List<DishExcelObjDto> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Menu");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Description", "Price", "Weight", "Calories", "Ingredients", "Cuisines", "Dietary specifics"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Fill data rows
            int rowNum = 1;
            for (DishExcelObjDto dish : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dish.getId());
                row.createCell(1).setCellValue(dish.getName());
                row.createCell(2).setCellValue(dish.getDescription());
                row.createCell(3).setCellValue(dish.getPrice());
                row.createCell(4).setCellValue(dish.getWeight());
                row.createCell(5).setCellValue(dish.getCalories());
                row.createCell(6).setCellValue(String.join(",", dish.getIngredients()));
                row.createCell(7).setCellValue(String.join(",", dish.getCuisines()));
                row.createCell(8).setCellValue(String.join(",", dish.getDietarySpecifics()));
            }

            // Write workbook to ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error exporting Excel file: " + e.getMessage(), e);
        }
    }

}
