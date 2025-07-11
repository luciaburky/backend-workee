package com.example.exceptions;

public class EntityAlreadyEnabledException extends RuntimeException {
    public EntityAlreadyEnabledException(String message) {
        super(message);
    }

}
