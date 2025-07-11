package com.example.exceptions;

public class EntityAlreadyDisabled extends RuntimeException {
    public EntityAlreadyDisabled(String message) {
        super(message);
    }

}
