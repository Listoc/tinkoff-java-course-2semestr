package edu.java.scrapper.exception;

public class LinkNotExistException extends RuntimeException {
    public LinkNotExistException() {
    }

    public LinkNotExistException(String message) {
        super(message);
    }
}
