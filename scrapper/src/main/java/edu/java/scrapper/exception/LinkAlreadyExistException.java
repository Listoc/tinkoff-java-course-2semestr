package edu.java.scrapper.exception;

public class LinkAlreadyExistException extends RuntimeException {
    public LinkAlreadyExistException() {
    }

    public LinkAlreadyExistException(String message) {
        super(message);
    }
}
