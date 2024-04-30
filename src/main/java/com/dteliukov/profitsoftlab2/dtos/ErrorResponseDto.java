package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Data transfer object (DTO) for representing an error response.
 */
@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    /*
    The error message.
    */
    private String message;
}
