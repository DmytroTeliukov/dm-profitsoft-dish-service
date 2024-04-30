package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.ByteArrayInputStream;


/**
 * Data transfer object (DTO) for the response of generating a report.
 */
@Getter
@AllArgsConstructor
public class GenerateReportResponseDto {
    /*
    The name of the generated report.
    */
    private String name;
    /**
     * The content of the generated report.
     */
    private ByteArrayInputStream content;
}
