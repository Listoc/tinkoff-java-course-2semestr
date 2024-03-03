package edu.java.scrapper.controller;

import edu.java.scrapper.exception.ChatAlreadyExistException;
import edu.java.scrapper.exception.ChatNotExistException;
import edu.java.scrapper.exception.LinkAlreadyExistException;
import edu.java.scrapper.exception.LinkNotExistException;
import edu.java.shared.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerHandler {
    private final static String BAD_REQUEST_STRING = "400";
    private final static String NOT_FOUND_STRING = "404";

    @ExceptionHandler(ChatAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse chatAlreadyExist(ChatAlreadyExistException e) {
        return new ApiErrorResponse("Chat already exist", BAD_REQUEST_STRING, e);
    }

    @ExceptionHandler(ChatNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatNotExist(ChatNotExistException e) {
        return new ApiErrorResponse("Chat does not exist", NOT_FOUND_STRING, e);
    }

    @ExceptionHandler(LinkAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse linkAlreadyExist(ChatAlreadyExistException e) {
        return new ApiErrorResponse("Link already exist", BAD_REQUEST_STRING, e);
    }

    @ExceptionHandler(LinkNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotExist(ChatNotExistException e) {
        return new ApiErrorResponse("Link does not exist", NOT_FOUND_STRING, e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse wrongPathVariable(MethodArgumentTypeMismatchException e) {
        return new ApiErrorResponse("Found non integer id", BAD_REQUEST_STRING, e);
    }

}
