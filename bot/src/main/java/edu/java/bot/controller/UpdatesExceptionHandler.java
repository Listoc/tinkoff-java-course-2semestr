package edu.java.bot.controller;

import edu.java.bot.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UpdatesExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> exceptionHandler(Exception e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                new ApiErrorResponse(
                    "Bad request parameters",
                    "400",
                    e.getClass().getName(),
                    e.getMessage(),
                    e.getStackTrace())
            );
    }
}
