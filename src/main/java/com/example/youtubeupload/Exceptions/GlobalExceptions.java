package com.example.youtubeupload.Exceptions;

import com.example.youtubeupload.Payloads.ErrorAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptions extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorAPIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        ErrorAPIResponse apiResponse = new ErrorAPIResponse("ResourceNotFoundException", ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
