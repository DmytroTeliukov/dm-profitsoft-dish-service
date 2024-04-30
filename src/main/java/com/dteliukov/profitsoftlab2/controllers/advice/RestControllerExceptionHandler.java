package com.dteliukov.profitsoftlab2.controllers.advice;

import com.dteliukov.profitsoftlab2.dtos.ErrorResponseDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handles method argument validation errors.
     *
     * @param e       The MethodArgumentNotValidException to handle.
     * @param headers The HTTP headers.
     * @param status  The HTTP status code.
     * @param request The web request.
     * @return ResponseEntity containing validation errors.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, status);
    }

    /**
     * Handles EntityNotFoundException.
     *
     * @param e The EntityNotFoundException to handle.
     * @return ErrorResponseDto containing the error message.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponseDto handlerEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    /**
     * Handles EntityExistsException.
     *
     * @param e The EntityExistsException to handle.
     * @return ErrorResponseDto containing the error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponseDto handlerEntityExistsException(EntityExistsException e) {
        return new ErrorResponseDto(e.getMessage());
    }
}
