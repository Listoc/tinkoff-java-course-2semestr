package edu.java.scrapper.exception;

public class CantAddToDBException extends RuntimeException {
    public CantAddToDBException() {
    }

    public CantAddToDBException(String message) {
        super(message);
    }
}
