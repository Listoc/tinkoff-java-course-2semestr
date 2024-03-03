package edu.java.bot.controller;

import edu.java.bot.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UpdatesExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse exceptionHandler(Exception e) {
        return new ApiErrorResponse(
                    "Bad request parameters",
                    "400",
                    e.getClass().getName(),
                    e.getMessage(),
                    e.getStackTrace());
    }
}
