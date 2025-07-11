package com.example.exceptions;

public class EntityNotValidException extends RuntimeException {
    public EntityNotValidException(String message) {
        super(message);
    }

}
