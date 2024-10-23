package com.example.virtualwinesommelierbackend.exception;

/**
 * Exception thrown when a requested entity is not found in the database
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
