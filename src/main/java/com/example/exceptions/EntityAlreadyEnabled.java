package com.example.exceptions;

public class EntityAlreadyEnabled extends RuntimeException {
    public EntityAlreadyEnabled(String message) {
        super(message);
    }

}
