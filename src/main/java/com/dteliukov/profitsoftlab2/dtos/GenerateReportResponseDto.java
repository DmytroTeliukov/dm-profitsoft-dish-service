package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.ByteArrayInputStream;

@Getter
@AllArgsConstructor
public class GenerateReportResponseDto {
    private String name;
    private ByteArrayInputStream content;
}
