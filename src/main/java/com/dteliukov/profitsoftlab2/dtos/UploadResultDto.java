package com.dteliukov.profitsoftlab2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Data transfer object (DTO) for representing the result of uploading data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultDto {
    /*
    The count of successful uploaded records.
    */
    private int countSuccessfulUploadedRecords;
    /**
     * The count of failed uploaded records.
     */
    private int countFailedUploadedRecords;
}
