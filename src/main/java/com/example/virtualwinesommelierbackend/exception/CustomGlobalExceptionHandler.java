package com.example.virtualwinesommelierbackend.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler to handle specific exceptions across the application.
 * This class handles validation errors and custom exceptions such as RegistrationException.
 */
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handles ConstraintViolationException that occurs during validation of method parameters
     * or property constraints.
     *
     * @param ex the ConstraintViolationException thrown when validation fails.
     * @return a map of validation errors where the key is the field/property path that caused
     * the violation,
     *         and the value is the corresponding validation error message.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    /**
     * Handles validation errors for method arguments that fail validation constraints.
     *
     * @param ex the exception that contains details about validation errors.
     * @param headers the HTTP headers to be returned with the response.
     * @param status the HTTP status code to be returned.
     * @param request the current web request.
     * @return a ResponseEntity containing error details and validation messages.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    /**
     * Handles RegistrationException when a user tries to register but there's a conflict.
     *
     * @param ex the exception thrown when there's a registration conflict.
     * @return a ResponseEntity containing the error message and HTTP status 409 (Conflict).
     */
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Extracts error messages from FieldError or ObjectError.
     * If the error is related to a specific field, it returns a formatted message with
     * the field name.
     *
     * @param e the error object containing validation details.
     * @return a formatted error message as a String.
     */
    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField(); // Extract the field with the error
            String message = e.getDefaultMessage(); // Extract the error message
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }
}
