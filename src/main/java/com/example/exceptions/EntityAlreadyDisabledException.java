package com.example.exceptions;

public class EntityAlreadyDisabledException extends RuntimeException {
    public EntityAlreadyDisabledException(String message) {
        super(message);
    }

}
