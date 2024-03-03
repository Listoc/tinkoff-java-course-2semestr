package edu.java.scrapper.controller;

import edu.java.scrapper.exception.ChatAlreadyExistException;
import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.shared.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class TelegramChatIdHandler {
    @ExceptionHandler(ChatAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse chatAlreadyExist(ChatAlreadyExistException e) {
        return new ApiErrorResponse("Chat already exist", "400", e);
    }

    @ExceptionHandler(ChatNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatNotExist(ChatNotExistException e) {
        return new ApiErrorResponse("Chat does not exist", "404", e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse wrongPathVariable(MethodArgumentTypeMismatchException e) {
        return new ApiErrorResponse("Found non integer id", "400", e);
    }

}
