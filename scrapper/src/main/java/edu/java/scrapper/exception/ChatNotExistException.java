package edu.java.scrapper.exception;

public class ChatNotExistException extends RuntimeException {
    public ChatNotExistException() {
    }

    public ChatNotExistException(String message) {
        super(message);
    }
}
