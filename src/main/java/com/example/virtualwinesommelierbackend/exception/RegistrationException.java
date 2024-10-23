package com.example.virtualwinesommelierbackend.exception;

/**
 * Exception thrown when a registration process fails, such as when a user with the same
 * email already exists.
 */
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
}
